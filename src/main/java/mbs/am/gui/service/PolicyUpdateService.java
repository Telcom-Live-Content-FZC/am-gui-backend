package mbs.am.gui.service;

import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.objects.ExtendedData;
import com.psi.mfsv4.mbs.common.objects.PaymentStatus;
import lombok.extern.jbosslog.JBossLog;
import lombok.var;
import mbs.softpos.common.AbstractRequest;
import mbs.softpos.common.JsonDataExtractor;
import mbs.softpos.common.MessageId;
import mbs.softpos.common.Messages;
import mbs.softpos.common.dto.RequestContext;
import mbs.softpos.common.dto.TransactionResponse;
import mbs.am.gui.common.TransactionInterceptor;
import mbs.am.gui.dto.PolicySaveRequest;
import mbs.am.gui.entity.PolicyHistoryEntity;
import mbs.am.gui.exception.ExceptionFactory;
import mbs.am.gui.mapper.SignalPolicyMapper;
import mbs.am.gui.model.SignalPolicy;
import mbs.am.gui.repository.SignalPolicyRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mbs.am.gui.entity.SignalPolicyEntity;

import javax.interceptor.Interceptors;


@JBossLog
@Stateless
public class PolicyUpdateService extends AbstractRequest {

    @EJB
    private SignalPolicyRepository policyRepository;

    @Inject
    private SignalPolicyMapper mapper;

    @Inject
    private ExceptionFactory exceptionFactory;


    @Override
    @Interceptors(TransactionInterceptor.class)
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));

        try {
            long policyId = Long.parseLong(context.getRequest().getPathParams().get(1));
            Map<String, Object> request = JsonDataExtractor.toMap(context.getPayload().getExtendedData());
            PolicySaveRequest policySaveRequest = JsonDataExtractor.fromJson(JsonDataExtractor.toJson(request), PolicySaveRequest.class);

            String tenantId = policySaveRequest.getTenantId();

            // 1. Fetch the existing ENTITY (This is now a "Managed" JPA Object)
            Optional<SignalPolicyEntity> optionalEntity = policyRepository.findByIdAndTenant(policyId, tenantId);
            if (!optionalEntity.isPresent()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_NOT_FOUND));
                tres.setDescription("Policy not found.");
                return createHttpResponse(tres, HttpStatus.SC_NOT_FOUND);
            }

            SignalPolicyEntity existingEntity = optionalEntity.get();
            boolean isUpdated = false;

            // 2. Compare fields and create History entries if they changed
            // Expected Value
            if (!existingEntity.getExpectedValue().equals(policySaveRequest.getExpectedValue())) {
                createAndSaveHistory(tenantId, policyId, "EXPECTED_VALUE", existingEntity.getExpectedValue(), policySaveRequest.getExpectedValue(), policySaveRequest.getUpdatedBy());
                existingEntity.setExpectedValue(policySaveRequest.getExpectedValue());
                isUpdated = true;
            }

            // Risk Weight
            if (!existingEntity.getRiskWeight().equals(policySaveRequest.getRiskWeight())) {
                createAndSaveHistory(tenantId, policyId, "RISK_WEIGHT", String.valueOf(existingEntity.getRiskWeight()), String.valueOf(policySaveRequest.getRiskWeight()), policySaveRequest.getUpdatedBy());
                existingEntity.setRiskWeight(policySaveRequest.getRiskWeight());
                isUpdated = true;
            }

            // Action
            if (!existingEntity.getAction().equals(policySaveRequest.getAction())) {
                createAndSaveHistory(tenantId, policyId, "ACTION", existingEntity.getAction(), policySaveRequest.getAction(), policySaveRequest.getUpdatedBy());
                existingEntity.setAction(policySaveRequest.getAction());
                isUpdated = true;
            }

            // Status
            if (!existingEntity.getStatus().equals(policySaveRequest.getStatus())) {
                createAndSaveHistory(tenantId, policyId, "STATUS", String.valueOf(existingEntity.getStatus()), String.valueOf(policySaveRequest.getStatus()), policySaveRequest.getUpdatedBy());
                existingEntity.setStatus(policySaveRequest.getStatus());
                isUpdated = true;
            }

            // 3. Save the entity if changes were detected
            SignalPolicyEntity savedEntity = existingEntity;
            if (isUpdated) {
                existingEntity.setUpdatedAt(LocalDateTime.now());
                // The BaseRepository.save() will handle the merge and flush
                savedEntity = policyRepository.save(existingEntity);
            }

            // 4. Map the updated Entity -> Model -> Response
            SignalPolicy updatedModel = mapper.toModel(savedEntity);

            ext.put("policy", mapper.toResponse(updatedModel));
            tres.setExtendedData(ext);
            tres.setStatus(PaymentStatus.SUCCESS);
            tres.setDescription("Policy updated successfully");

            return createHttpResponse(tres);

        } catch (Exception e) {
            captureException(e);
            tres.setStatus(String.valueOf(MessageId.ERR_MESSAGE));
            tres.setDescription(Messages.getErrorMessage(MessageId.ERR_MESSAGE));
            return createHttpResponse(tres, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createAndSaveHistory(String tenantId, Long policyId, String field, String oldVal, String newVal, String changedBy) {
        var policyHistory = PolicyHistoryEntity.builder()
                .tenantId(tenantId)
                .policyId(policyId)
                .fieldChanged(field)
                .oldValue(oldVal)
                .newValue(newVal)
                .changedBy(changedBy != null ? changedBy : "SYSTEM")
                .changedAt(LocalDateTime.now())
                .build();

        policyRepository.insertHistory(policyHistory);
    }

    @Override
    protected String[] required() {
        // TODO Auto-generated method stub
        return new String[]{};

    }

    @Override
    protected String[] requiredHeader() {
        // TODO Auto-generated method stub
        return new String[]{};
    }

    @Override
    protected Map<String, String[]> requiredExtendedSections() {
        // TODO Auto-generated method stub
        Map<String, String[]> ext = new HashMap<String, String[]>();
        return ext;
    }
}
