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
import java.util.Optional;

@JBossLog
@Stateless
public class TenantUpdateService extends AbstractRequest {

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
            String idStr = context.getRequest().getPathParams().get(1);

            Map<String, Object> request = JsonDataExtractor.toMap(context.getPayload().getExtendedData());
            TenantDto tenantRequest = JsonDataExtractor.fromJson(JsonDataExtractor.toJson(request), TenantDto.class);

            Optional<TenantEntity> optionalTenant = tenantRepository.findById(Long.parseLong(idStr));
            if (!optionalTenant.isPresent()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_NOT_FOUND));
                tres.setDescription("Tenant not found.");
                return createHttpResponse(tres, HttpStatus.SC_NOT_FOUND);
            }

            TenantEntity oldTenant = optionalTenant.get();

            Tenant updateModel = mapper.fromDto(tenantRequest);
            updateModel.setId(oldTenant.getId());

            TenantEntity entityToUpdate = mapper.toEntity(updateModel);

            entityToUpdate.setCreatedAt(oldTenant.getCreatedAt());

            TenantEntity savedEntity = tenantRepository.save(entityToUpdate);

            ext.put("tenant", mapper.toDto(mapper.fromEntity(savedEntity)));
            tres.setExtendedData(ext);
            tres.setStatus(PaymentStatus.SUCCESS);
            tres.setDescription("Tenant updated successfully");

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
