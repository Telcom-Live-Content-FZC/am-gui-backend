package mbs.am.gui.service;

import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.objects.PaymentStatus;
import lombok.extern.jbosslog.JBossLog;
import mbs.am.gui.common.SystemUtil;
import mbs.am.gui.common.TransactionInterceptor;
import mbs.am.gui.exception.MessageLookupService;
import mbs.am.gui.model.PolicyState;
import mbs.am.gui.repository.SignalPolicyRepository;
import mbs.softpos.common.AbstractRequest;
import mbs.softpos.common.MessageId;
import mbs.softpos.common.dto.RequestContext;
import mbs.softpos.common.dto.TransactionResponse;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@JBossLog
@Stateless
public class RejectPolicyService extends AbstractRequest {

    @EJB
    private SignalPolicyRepository policyRepository;

    @Inject
    private MessageLookupService messageLookupService;

    @Override
    @Interceptors(TransactionInterceptor.class)
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));
        try {

            Long tenantId = SystemUtil.parseLongSafely(context.getPayload().getExtendedData().get("tenant-id")).orElse(null);
            String version = (String) context.getPayload().getExtendedData().get("version");

            Optional<Integer> currentStatusOpt = policyRepository.getVersionStatus(tenantId, version);

            if (!currentStatusOpt.isPresent()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_NOT_FOUND));
                tres.setDescription("Validation Failed: Version " + version + " does not exist.");
                return createHttpResponse(tres, HttpStatus.SC_NOT_FOUND);
            }
            PolicyState currentState = PolicyState.fromCode(currentStatusOpt.get());

            if (currentState != PolicyState.PENDING) {
                tres.setStatus(String.valueOf(HttpStatus.SC_BAD_REQUEST));
                tres.setDescription("Validation Failed:  Version " + version + " is currently " + currentState.name() + ".");
                return createHttpResponse(tres, HttpStatus.SC_BAD_REQUEST);
            }

            policyRepository.updateStatusByVersion(tenantId, version, PolicyState.REJECTED.getCode());
            tres.setDescription("Version " + version + " has been REJECTED.");
            tres.setStatus(PaymentStatus.SUCCESS);
            return createHttpResponse(tres);

        } catch (Exception e) {
            captureException(e);
            tres.setStatus(String.valueOf(MessageId.ERR_MESSAGE));
            tres.setDescription(messageLookupService.getFormattedError(MessageId.ERR_MESSAGE).getMessage());
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
