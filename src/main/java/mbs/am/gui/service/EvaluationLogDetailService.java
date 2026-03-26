package mbs.am.gui.service;
import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.objects.ExtendedData;
import com.psi.mfsv4.mbs.common.objects.PaymentStatus;
import lombok.extern.jbosslog.JBossLog;
import mbs.am.gui.common.SystemUtil;
import mbs.softpos.common.AbstractRequest;
import mbs.softpos.common.MessageId;
import mbs.softpos.common.Messages;
import mbs.softpos.common.dto.RequestContext;
import mbs.softpos.common.dto.TransactionResponse;
import mbs.am.gui.dto.EvaluationDetailResponse;
import mbs.am.gui.dto.TriggeredRule;
import mbs.am.gui.entity.RiskEvaluationEntity;
import mbs.am.gui.repository.RiskEvaluationRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JBossLog
@Stateless
public class EvaluationLogDetailService extends AbstractRequest {

    @EJB
    private RiskEvaluationRepository riskEvaluationRepository;

    @Override
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();
        HttpResponse res = new HttpResponse(HttpStatus.SC_OK);

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));

        try {

            Long tenantId = SystemUtil.parseLongSafely(context.getRequest().getQueryParams("tenant-id"))
                    .orElse(null);
            Optional<Long> evalIdOpt = SystemUtil.parseLongSafely(context.getRequest().getQueryParams("evaluation-id"));

            if (!evalIdOpt.isPresent()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_BAD_REQUEST));
                tres.setDescription("A valid numeric evaluation-id is required");
                return createHttpResponse(tres, HttpStatus.SC_BAD_REQUEST);
            }
            long evaluationId = evalIdOpt.get();

            Optional<RiskEvaluationEntity> evaluationEntity =
                    riskEvaluationRepository.getEvaluationById(evaluationId, tenantId);

            if (!evaluationEntity.isPresent()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_NOT_FOUND));
                tres.setDescription("Evaluation record not found.");
                return createHttpResponse(tres, HttpStatus.SC_NOT_FOUND);
            }
            RiskEvaluationEntity entity = evaluationEntity.get();

            List<TriggeredRule> rules = riskEvaluationRepository.getTriggeredRulesForEvaluation(evaluationId);

            EvaluationDetailResponse detailDto = EvaluationDetailResponse.builder()
                    .evaluationId(entity.getId().toString())
                    .evaluationTime(entity.getEvaluationTime())
                    .deviceId(entity.getDeviceId())
                    .finalAction(entity.getFinalAction())
                    .totalRiskScore(entity.getTotalRiskScore())
                    .triggeredRules(rules)
                    .build();

            ext.put("evaluation-detail", detailDto);

            tres.setExtendedData(ext);
            tres.setStatus(PaymentStatus.SUCCESS);
            tres.setDescription(Messages.getErrorMessage(MessageId.OK_MESSAGE_SUCCESS));
            res = createHttpResponse(tres);

            return res;
        } catch (Exception e) {
            captureException(e);
            tres.setStatus(String.valueOf(MessageId.ERR_MESSAGE));
            tres.setDescription(Messages.getErrorMessage(MessageId.ERR_MESSAGE));
            return createHttpResponse(tres, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    protected String[] required() {
        // TODO Auto-generated method stub
        return new String[] {};

    }

    @Override
    protected String[] requiredHeader() {
        // TODO Auto-generated method stub
        return new String[] {};
    }

    @Override
    protected Map<String, String[]> requiredExtendedSections() {
        // TODO Auto-generated method stub
        Map<String, String[]> ext = new HashMap<String, String[]>();
        return ext;
    }
}
