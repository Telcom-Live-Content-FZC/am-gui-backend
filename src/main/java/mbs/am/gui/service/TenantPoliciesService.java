package mbs.am.gui.service;

import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.objects.ExtendedData;
import com.psi.mfsv4.mbs.common.objects.PaymentStatus;
import lombok.extern.jbosslog.JBossLog;

import mbs.softpos.common.AbstractRequest;
import mbs.softpos.common.MessageId;
import mbs.softpos.common.Messages;
import mbs.softpos.common.dto.RequestContext;
import mbs.softpos.common.dto.TransactionResponse;
import mbs.am.gui.dto.PolicyResponse;
import mbs.am.gui.entity.SignalPolicyEntity;
import mbs.am.gui.mapper.SignalPolicyMapper;
import mbs.am.gui.repository.SignalPolicyRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@JBossLog
@Stateless
public class TenantPoliciesService extends AbstractRequest {

    @EJB
    private SignalPolicyRepository policyRepository;

    @Inject
    private SignalPolicyMapper mapper;

    @Override
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));

        try {
            String rawTenantId = context.getRequest().getQueryParams("tenant-id");
            String tenantId = (rawTenantId != null) ? rawTenantId.toUpperCase() : null;

            List<SignalPolicyEntity> entities;
            if (tenantId == null || tenantId.trim().isEmpty()) {
                entities = policyRepository.findAllCrossTenant();
            } else {
                entities = policyRepository.findAllByTenant(tenantId);
            }

            if (entities == null || entities.isEmpty()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_BAD_REQUEST));
                tres.setDescription("tenant-id not found or no policies available.");
                return createHttpResponse(tres, HttpStatus.SC_BAD_REQUEST);
            }

            List<PolicyResponse> responses = entities.stream()
                    .map(mapper::toModel)
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());

            ext.put("policies", responses);
            tres.setExtendedData(ext);
            tres.setStatus(PaymentStatus.SUCCESS);
            tres.setDescription(Messages.getErrorMessage(MessageId.OK_MESSAGE_SUCCESS));

            return createHttpResponse(tres);

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
