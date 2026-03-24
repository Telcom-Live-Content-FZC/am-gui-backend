package mbs.workflow.am.gui.service;

import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.objects.ExtendedData;
import com.psi.mfsv4.mbs.common.objects.PaymentStatus;
import lombok.extern.jbosslog.JBossLog;
import mbs.softpos.common.AbstractRequest;
import mbs.softpos.common.JsonDataExtractor;
import mbs.softpos.common.MessageId;
import mbs.softpos.common.Messages;
import mbs.softpos.common.dto.RequestContext;
import mbs.softpos.common.dto.TransactionResponse;
import mbs.workflow.am.gui.common.TransactionInterceptor;
import mbs.workflow.am.gui.dto.TenantDto;
import mbs.workflow.am.gui.entity.TenantEntity;
import mbs.workflow.am.gui.mapper.TenantMapper;
import mbs.workflow.am.gui.model.Tenant;
import mbs.workflow.am.gui.repository.TenantRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.HashMap;
import java.util.Map;

@JBossLog
@Stateless
public class RegisterTenantService extends AbstractRequest {

    @EJB
    private TenantRepository tenantRepository;

    @Inject
    private TenantMapper mapper;

    @Override
    @Interceptors(TransactionInterceptor.class)
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));

        try {
            Map<String, Object> request = JsonDataExtractor.toMap(context.getPayload().getExtendedData());
            TenantDto tenantRequest = JsonDataExtractor.fromJson(JsonDataExtractor.toJson(request), TenantDto.class);

            // Map DTO -> Model -> Entity
            Tenant model = mapper.fromDto(tenantRequest);
            TenantEntity entityToSave = mapper.toEntity(model);

            // Save Entity
            TenantEntity savedEntity = tenantRepository.save(entityToSave);

            // Map Entity -> Model -> DTO for response
            Tenant savedModel = mapper.fromEntity(savedEntity);
            ext.put("tenant", mapper.toDto(savedModel));

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
