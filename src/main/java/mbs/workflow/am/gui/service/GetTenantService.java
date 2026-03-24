package mbs.workflow.am.gui.service;

import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.objects.ExtendedData;
import com.psi.mfsv4.mbs.common.objects.PaymentStatus;
import lombok.extern.jbosslog.JBossLog;
import mbs.softpos.common.AbstractRequest;
import mbs.softpos.common.MessageId;
import mbs.softpos.common.Messages;
import mbs.softpos.common.dto.RequestContext;
import mbs.softpos.common.dto.TransactionResponse;
import mbs.workflow.am.gui.dto.TenantDto;
import mbs.workflow.am.gui.entity.TenantEntity;
import mbs.workflow.am.gui.mapper.TenantMapper;
import mbs.workflow.am.gui.model.Tenant;
import mbs.workflow.am.gui.repository.TenantRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@JBossLog
@Stateless
public class GetTenantService extends AbstractRequest {

    @EJB
    private TenantRepository tenantRepository;

    @Inject
    private TenantMapper mapper;

    @Override
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));

        try {
            String tenantId = context.getRequest().getQueryParams("tenant-id");
            List<TenantEntity> entities = tenantId != null && !tenantId.trim().isEmpty() ?
                     tenantRepository.findById(Long.parseLong(tenantId))
                             .map(Collections::singletonList)
                             .orElseGet(Collections::emptyList) : tenantRepository.findAll();

            if (entities == null || entities.isEmpty()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_NOT_FOUND));
                tres.setDescription("Tenant(s) not found.");
                return createHttpResponse(tres, HttpStatus.SC_NOT_FOUND);
            }

            List<Tenant> models = mapper.fromEntities(entities);
            List<TenantDto> dtos = mapper.toDtos(models);

            ext.put("tenants", dtos);
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
        return new String[]{};
    }

    @Override
    protected String[] requiredHeader() {
        return new String[]{};
    }

    @Override
    protected Map<String, String[]> requiredExtendedSections() {
        return new HashMap<String, String[]>();
    }
}