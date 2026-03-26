package mbs.am.gui.service;

import com.google.gson.reflect.TypeToken;
import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.objects.ExtendedData;
import com.psi.mfsv4.mbs.common.objects.PaymentStatus;
import lombok.extern.jbosslog.JBossLog;
import mbs.am.gui.common.SystemUtil;
import mbs.am.gui.common.TransactionInterceptor;
import mbs.am.gui.dto.PolicySaveRequest;
import mbs.am.gui.entity.SignalPolicyEntity;
import mbs.am.gui.exception.MessageLookupService;
import mbs.am.gui.mapper.SignalPolicyMapper;
import mbs.am.gui.repository.SignalPolicyRepository;
import mbs.softpos.common.AbstractRequest;
import mbs.softpos.common.JsonDataExtractor;
import mbs.softpos.common.MessageId;
import mbs.softpos.common.dto.RequestContext;
import mbs.softpos.common.dto.TransactionResponse;
import org.apache.http.HttpStatus;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@JBossLog
@Stateless
public class RegisterPolicyService extends AbstractRequest {

    @EJB
    private SignalPolicyRepository policyRepository;

    @Inject
    private SignalPolicyMapper mapper;

    @Inject
    private MessageLookupService messageLookupService;

    @Override
    @Interceptors(TransactionInterceptor.class)
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));
        try {

            String version = (String) context.getPayload().getExtendedData().get("version");
            Long tenantId = SystemUtil.parseLongSafely(
                    context.getPayload().getExtendedData().get("tenant-id")
            ).orElse(null);

            List<PolicySaveRequest> policies = JsonDataExtractor.extractList(
                    context.getPayload().getExtendedData(),
                    "policies",
                    PolicySaveRequest.class
            );
            log.infof(JsonDataExtractor.toJson(policies));
            LocalDateTime now = LocalDateTime.now();
            for (PolicySaveRequest policy : policies) {
                SignalPolicyEntity entity = mapper.toEntity(mapper.toModel(policy));
                entity.setTenantId(tenantId);
                entity.setVersion(version);
                entity.setStatus(2);
                entity.setCreatedAt(now);
                policyRepository.saveWithCatalog(entity, policy.getKey());
            }
            tres.setStatus(PaymentStatus.SUCCESS);
            tres.setDescription("Version " + version + " registered successfully. Status: PENDING");
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
