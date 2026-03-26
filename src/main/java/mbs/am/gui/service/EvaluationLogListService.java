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
import mbs.am.gui.dto.EvaluationLog;
import mbs.am.gui.repository.RiskEvaluationRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JBossLog
@Stateless
public class EvaluationLogListService extends AbstractRequest {

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
            String deviceId = context.getRequest().getQueryParams("device-id");
            String action = context.getRequest().getQueryParams("action");



            int page = SystemUtil.parseIntSafely(context.getRequest().getQueryParams("page"))
                    .filter(p -> p > 0)
                    .orElse(1);

            int limit = SystemUtil.parseIntSafely(context.getRequest().getQueryParams("limit"))
                    .filter(l -> l > 0 && l <= 100)
                    .orElse(50);

            int offset = (page - 1) * limit;

            List<EvaluationLog> logs = riskEvaluationRepository.getEvaluationLogs(
                    tenantId,
                    SystemUtil.isNotBlank(deviceId) ? deviceId : null,
                    SystemUtil.isNotBlank(action) ? action : null,
                    offset, limit
            );

            ext.put("evaluations", logs);
            ext.put("page", page);
            ext.put("limit", limit);

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
