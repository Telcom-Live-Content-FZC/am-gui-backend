package mbs.am.gui.service;

import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.objects.ExtendedData;
import com.psi.mfsv4.mbs.common.objects.PaymentStatus;
import lombok.extern.jbosslog.JBossLog;
import lombok.var;
import mbs.am.gui.common.SystemUtil;
import mbs.softpos.common.AbstractRequest;
import mbs.softpos.common.MessageId;
import mbs.softpos.common.Messages;
import mbs.softpos.common.dto.RequestContext;
import mbs.softpos.common.dto.TransactionResponse;
import mbs.am.gui.repository.RiskEvaluationRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@JBossLog
@Stateless
public class TopThreatsService extends AbstractRequest {

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

            int daysBack = SystemUtil.parseIntSafely(context.getRequest().getQueryParams("days-back"))
                    .filter(d -> d > 0)
                    .orElse(7);

            int limit = SystemUtil.parseIntSafely(context.getRequest().getQueryParams("limit"))
                    .filter(l -> l > 0 && l <= 100)
                    .orElse(10);


            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(daysBack);

            var listOfTopThreats =  riskEvaluationRepository.getTopThreats(tenantId, startDate, endDate, limit);
            ext.put("top-threats", listOfTopThreats);
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
