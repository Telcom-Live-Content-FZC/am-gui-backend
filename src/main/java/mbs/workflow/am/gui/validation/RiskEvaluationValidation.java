package mbs.workflow.am.gui.validation;

import com.psi.common.util.StringUtil;
import com.psi.common.util.SystemUtil;
import com.psi.mfsv4.mbs.common.objects.ExtendedData;
import lombok.extern.jbosslog.JBossLog;
import mbs.softpos.common.AbstractValueConstants;
import mbs.softpos.common.CertificateParser;
import mbs.softpos.common.MessageId;
import mbs.softpos.common.Messages;
import mbs.softpos.common.dao.IntegInfoDaoCache;
import mbs.softpos.common.dto.ServiceValidationDto;
import mbs.softpos.common.dto.TransactionResponse;
import mbs.workflow.am.gui.domain.RiskEvaluation;
import mbs.workflow.am.gui.domain.RiskEvaluationDetail;
import mbs.workflow.am.gui.dto.DeviceContext;
import mbs.workflow.am.gui.dto.RiskEvaluationRequest;
import mbs.workflow.am.gui.dto.RiskEvaluationResponse;
import mbs.workflow.am.gui.exception.*;
import mbs.workflow.am.gui.mapper.RiskEngineMapper;
import mbs.workflow.am.gui.mapper.RiskEvaluationMapper;
import mbs.workflow.am.gui.mapper.RiskThresholdMapper;
import mbs.workflow.am.gui.mapper.SignalPolicyMapper;
import mbs.workflow.am.gui.model.RiskConfig;
import mbs.workflow.am.gui.model.RiskThreshold;
import mbs.workflow.am.gui.model.SignalPolicy;
import mbs.workflow.am.gui.repository.*;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@JBossLog
public class RiskEvaluationValidation {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Inject
    private IntegInfoDaoCache integInfoDaoCache;

    @EJB
    private RiskEvaluationRepository riskRepo;
    @EJB
    private SignalPolicyRepository signalPolicyRepo;
    @EJB
    private RiskThresholdRepository riskThresholdRepo;
    @EJB
    private RiskConfigRepository riskConfigRepo;

    @Inject
    private RiskEvaluationMapper riskMapper;
    @Inject
    private RiskEngineMapper riskEngineMapper;
    @Inject
    private SignalPolicyMapper signalPolicyMapper;
    @Inject
    private RiskThresholdMapper riskThresholdMapper;
    @Inject
    private ExceptionFactory exceptionFactory;
    @Inject
    private MessageLookupService messageService;

    public ServiceValidationDto<RiskEvaluationResponse> evaluateRisk(RiskEvaluationRequest request) {
        try {
            // Resolve basic configuration
            String tenantId = (request.getTenantId() != null) ? request.getTenantId() : "DEFAULT";

            RiskConfig config = riskConfigRepo.findByTenantId(tenantId)
                    .map(riskEngineMapper::toModel)
                    .orElseGet(() -> riskConfigRepo.findByTenantId("DEFAULT")
                            .map(riskEngineMapper::toModel)
                            .orElseThrow(() -> exceptionFactory.createError(GenericException.class, 7, tenantId)));

            // Initialize Domain Model
            RiskEvaluation evaluationModel = RiskEvaluation.builder()
                    .requestId(request.getRequestId())
                    .tenantId(tenantId)
                    .deviceId(request.getDeviceId())
                    .appVersion(request.getAppVersion())
                    .details(new ArrayList<>())
                    .totalRiskScore(0)
                    .build();

            if (!config.isEngineEnabled()) {
                evaluationModel.setFinalAction("ALLOW");
                return ServiceValidationDto.ok(riskMapper.toResponse(evaluationModel));
            }

            //  Gather all Signals
            Map<String, Object> incomingSignals = new HashMap<>();
            if (request.getThreatMap() != null) incomingSignals.putAll(request.getThreatMap());
            incomingSignals.put("rooted-device", request.isRooted());
            incomingSignals.put("time-offset-seconds", calculateTimeOffsetSeconds(request.getDateTime(), formatter));

            String os = request.getOs().toLowerCase();
            String version = request.getOsVersion();

            incomingSignals.put(os + "-supported-versions", version);
            String patchBaseKey = String.format("%s-%s-patch",os,version);
            incomingSignals.put(patchBaseKey + "-min", request.getPatchLevel());
            incomingSignals.put(patchBaseKey + "-max", request.getPatchLevel());

            // Resolve Policies
            Map<String, SignalPolicy> activePolicies = new HashMap<>();

            signalPolicyRepo.findActiveByTenant("DEFAULT").stream()
                    .map(signalPolicyMapper::toModel)
                    .forEach(p -> activePolicies.put(p.getSignalKey(), p));

            if (!"DEFAULT".equals(tenantId)) {
                signalPolicyRepo.findActiveByTenant(tenantId).stream()
                        .map(signalPolicyMapper::toModel)
                        .forEach(p -> activePolicies.put(p.getSignalKey(), p));
            }

            //  Dynamic Evaluation & Scoring
            int currentScore = 0;
            boolean immediateBlock = false;
            boolean rootDetected = false;
            SignalPolicy primaryBlockingPolicy = null;
            Object primaryBlockingActualValue = null;

            for (SignalPolicy policy : activePolicies.values()) {
                String key = policy.getSignalKey();
                if (incomingSignals.containsKey(key)) {
                    Object actualValue = incomingSignals.get(key);
                    boolean isTriggered = evaluateDynamicRule(actualValue, policy.getOperator(),
                            policy.getExpectedValue(), policy.getDataType());

                    if (isTriggered) {
                        currentScore += policy.getRiskWeight();

                        // Add to Domain details (for DB)
                        evaluationModel.addDetail(new RiskEvaluationDetail(key, policy.getRiskWeight()));
                        if ("ROOT".equalsIgnoreCase(policy.getCategory())) {
                            rootDetected = true;
                        }
                        // Capture the FIRST policy that triggers a block so we can use its specific MSGID later
                        if (("BLOCK_DEVICE".equals(policy.getAction()) || "QUARANTINE".equals(policy.getAction()))
                                && primaryBlockingPolicy == null) {
                            immediateBlock = true;
                            primaryBlockingPolicy = policy;
                            primaryBlockingActualValue = actualValue;
                        }
                    }
                }
            }

            // Final Score Computation
            LocalDateTime windowStart = LocalDateTime.now().minusHours(config.getMonitorWindowHours());
            int historicalScore = riskRepo.getTotalRiskForDevice(request.getDeviceId(), tenantId, windowStart);
            int totalRiskScore = currentScore + historicalScore;
            evaluationModel.setTotalRiskScore(totalRiskScore);

            // Determine Action
            String action = "ALLOW";
            int thresholdLimit = 0;


            if (immediateBlock) {
                action = primaryBlockingPolicy.getAction(); // Use the exact action from the policy
            } else {
                // Fetch and Map Thresholds
                List<RiskThreshold> thresholds = riskThresholdRepo.findActiveByTenant(tenantId).stream()
                        .map(riskThresholdMapper::toModel)
                        .collect(Collectors.toList());

                if (thresholds.isEmpty() && !"DEFAULT".equals(tenantId)) {
                    thresholds = riskThresholdRepo.findActiveByTenant("DEFAULT").stream()
                            .map(riskThresholdMapper::toModel)
                            .collect(Collectors.toList());
                }

                for (RiskThreshold tier : thresholds) {
                    if (totalRiskScore >= tier.getMinScore() && totalRiskScore <= tier.getMaxScore()) {
                        action = tier.getAction();
                        thresholdLimit = tier.getMaxScore();
                        break;
                    }
                }
            }
            evaluationModel.setFinalAction(action);

            // Database Persistence
            riskRepo.save(riskMapper.toEntity(evaluationModel));

            // Generate Response DTO for the SDK
            RiskEvaluationResponse riskResponse = riskMapper.toResponse(evaluationModel);
            riskResponse.setThresholdUsed(thresholdLimit);
            riskResponse.setRootDetected(rootDetected);
            riskResponse.setDeviceContext(DeviceContext.builder()
                    .os(request.getOs())
                    .osVersion(request.getOsVersion())
                    .patchLevel(request.getPatchLevel())
                    .isRooted(request.isRooted())
                    .build());

            // Decide Outcome
            if ("BLOCK_DEVICE".equals(action) || "BLOCK".equals(action) || "QUARANTINE".equals(action)) {
                log.warnf("Risk action triggered: %s for device %s. Score: %d",
                        action, request.getDeviceId(), totalRiskScore);

                // Check if the block was caused by a specific policy rule, or by reaching the Threshold Score limit
                ErrorMessageEntity errorEntity;
                if (primaryBlockingPolicy != null && primaryBlockingPolicy.getMessageId() != null) {
                    errorEntity = messageService.getFormattedError(
                            primaryBlockingPolicy.getMessageId(),
                            primaryBlockingActualValue,
                            primaryBlockingPolicy.getExpectedValue());
                } else {
                    int fallbackMsgId = "QUARANTINE".equals(action) ? 15 : 17;
                    errorEntity = messageService.getFormattedError(fallbackMsgId);
                }
                TransactionResponse tres = new TransactionResponse();
                ExtendedData ext = new ExtendedData();
                ext.put("risk-evaluation", riskResponse);
                tres.setExtendedData(ext);
                tres.setStatus(String.valueOf(errorEntity.getCode()));
                tres.setDescription(errorEntity.getMessage());
                return ServiceValidationDto.fail(tres, errorEntity.getHttpStatus());
            }
            return ServiceValidationDto.ok(riskResponse);

        } catch (BaseException e) {
            log.warnf("Risk Evaluation aborted: [%d] %s", e.getErrorCode(), e.getMessage());
            TransactionResponse tres = new TransactionResponse();
            tres.setStatus(String.valueOf(e.getErrorCode()));
            tres.setDescription(e.getMessage());
            return ServiceValidationDto.fail(tres, e.getHttpStatus());
        } catch (Exception e) {
            log.error("Critical error in Risk Evaluation Engine", e);
            return ServiceValidationDto.fail(MessageId.ERR_MESSAGE,
                    Messages.getErrorMessage(MessageId.ERR_MESSAGE), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    private boolean evaluateDynamicRule(Object actualValue, String operator, String expectedValue, String dataType) {
        if (actualValue == null) return false;

        try {
            switch (dataType.toUpperCase()) {
                case "BOOLEAN":
                    boolean actualBool = (Boolean) actualValue;
                    boolean expectedBool = Boolean.parseBoolean(expectedValue);
                    return operator.equals("=") ? (actualBool == expectedBool) : (actualBool != expectedBool);

                case "NUMBER":
                case "VERSION": // Simple version comparison (can be expanded for complex semver)
                    double actualNum = Double.parseDouble(actualValue.toString());
                    double expectedNum = Double.parseDouble(expectedValue);
                    switch (operator) {
                        case "=": return actualNum == expectedNum;
                        case "!=": return actualNum != expectedNum;
                        case ">": return actualNum > expectedNum;
                        case "<": return actualNum < expectedNum;
                        case ">=": return actualNum >= expectedNum;
                        case "<=": return actualNum <= expectedNum;
                    }
                    break;

                case "DATE":
                    LocalDate actualDate = LocalDate.parse(actualValue.toString(), DateTimeFormatter.ISO_DATE);
                    LocalDate expectedDate = LocalDate.parse(expectedValue, DateTimeFormatter.ISO_DATE);
                    switch (operator) {
                        case "=": return actualDate.isEqual(expectedDate);
                        case ">": return actualDate.isAfter(expectedDate);
                        case "<": return actualDate.isBefore(expectedDate);
                    }
                    break;

                case "STRING":
                    String actualStr = actualValue.toString();
                    switch (operator) {
                        case "=": return actualStr.equals(expectedValue);
                        case "!=": return !actualStr.equals(expectedValue);
                        case "CONTAINS": return actualStr.contains(expectedValue);
                        case "IN": return java.util.Arrays.asList(expectedValue.split(",")).contains(actualStr);
                        case "NOT IN": return !java.util.Arrays.asList(expectedValue.split(",")).contains(actualStr);
                    }
                    break;
            }
        } catch (Exception e) {
            log.errorf("Failed to evaluate rule. Actual: %s, Expected: %s, Type: %s", actualValue, expectedValue, dataType);
            return true; // Fail-secure: If parsing fails, flag it as a risk.
        }
        return false;
    }

    private long calculateTimeOffsetSeconds(String dateTime, DateTimeFormatter format) {
        if (dateTime == null || dateTime.isEmpty()) {
            return Long.MAX_VALUE; // Force a massive offset to trigger failure
        }
        try {
            LocalDateTime requestTime = LocalDateTime.parse(dateTime, format);
            LocalDateTime serverTime = LocalDateTime.now();
            return Math.abs(Duration.between(requestTime, serverTime).getSeconds());
        } catch (Exception e) {
            log.error("Failed to parse device datetime", e);
            return Long.MAX_VALUE;
        }
    }


}
