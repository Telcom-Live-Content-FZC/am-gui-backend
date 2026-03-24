package mbs.am.gui.service;

import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.objects.ExtendedData;
import com.psi.mfsv4.mbs.common.objects.PaymentStatus;
import lombok.extern.jbosslog.JBossLog;
import lombok.var;
import mbs.softpos.common.*;
import mbs.softpos.common.dto.RequestContext;
import mbs.softpos.common.dto.TransactionResponse;
import mbs.am.gui.dto.ActionAccount;
import mbs.am.gui.dto.DashboardSummary;
import mbs.am.gui.dto.TopThreat;
import mbs.am.gui.repository.RiskEvaluationRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JBossLog
@Stateless
public class DashBoardService extends AbstractRequest {

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
            String tenantId = context.getRequest().getQueryParams("tenant-id");
            String daysBack = context.getRequest().getQueryParams("days-back");

            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(Integer.parseInt(daysBack));

            List<ActionAccount> actionResults = riskEvaluationRepository.getActionCounts(tenantId, startDate, endDate);

            long allowed = 0, blocked = 0, quarantined = 0, total = 0;

            for (ActionAccount actionAccount : actionResults) {
                String action = actionAccount.getFinalAction();
                long count = actionAccount.getCount();
                total += count;

                if ("ALLOW".equals(action)) allowed = count;
                else if ("BLOCK_DEVICE".equals(action)) blocked = count;
                else if ("QUARANTINE".equals(action)) quarantined = count;
            }

            double blockRate = (total == 0) ? 0.0 : ((double) blocked / total) * 100.0;

            // 2. Fetch Top Threats
            List<TopThreat> threatResults = riskEvaluationRepository.getTopThreats(tenantId, startDate, endDate, 5);
            List<TopThreat> topThreats = new ArrayList<>();

            for (TopThreat topThreat : threatResults) {
                String policyKey = topThreat.getKey();
                String description = topThreat.getDescription();
                long triggerCount = topThreat.getTriggerCount();

                topThreats.add(TopThreat.builder()
                        .key(policyKey)
                        .description(description)
                        .triggerCount(triggerCount)
                        .build());
            }
            var dashBoardSummary = DashboardSummary.builder()
                    .totalEvaluations(total)
                    .allowedCount(allowed)
                    .blockedCount(blocked)
                    .quarantinedCount(quarantined)
                    .blockRatePercentage(Math.round(blockRate * 100.0) / 100.0)
                    .topThreats(topThreats)
                    .build();

            ext.put("summary", dashBoardSummary);
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
