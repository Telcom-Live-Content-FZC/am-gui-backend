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
import mbs.am.gui.dto.TenantDto;
import mbs.am.gui.entity.TenantEntity;
import mbs.am.gui.mapper.TenantMapper;
import mbs.am.gui.model.Tenant;
import mbs.am.gui.repository.TenantRepository;
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
            Optional<Long> tenantIdOpt = SystemUtil.parseLongSafely(context.getRequest().getQueryParams("tenant-id"));

            List<TenantEntity> entities = tenantIdOpt.map(tenantId -> tenantRepository.findById(tenantId)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList())).orElseGet(() -> tenantRepository.findAll());

            if (entities.isEmpty()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_NOT_FOUND));
                tres.setDescription("No tenant data found.");
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