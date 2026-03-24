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
import mbs.workflow.am.gui.dto.DeviceRegistrationRequest;
import mbs.workflow.am.gui.entity.DeviceEntity;
import mbs.workflow.am.gui.exception.BaseException;
import mbs.workflow.am.gui.exception.ExceptionFactory;
import mbs.workflow.am.gui.exception.GenericException;
import mbs.workflow.am.gui.mapper.DeviceMapper;
import mbs.workflow.am.gui.repository.DeviceRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.HashMap;
import java.util.Map;

@JBossLog
@Stateless
public class DeviceUpdateService extends AbstractRequest {

    @EJB
    private DeviceRepository deviceRepository;

    @Inject
    private DeviceMapper mapper;

    @Inject
    private ExceptionFactory exceptionFactory;

    @Override
    @Interceptors(TransactionInterceptor.class)
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));

        try {
            String idStr = context.getRequest().getPathParams().get(1);

            if (idStr == null || idStr.trim().isEmpty()) {
                throw exceptionFactory.createError(GenericException.class, 4, "id");
            }

            long dbId = Long.parseLong(idStr);

            DeviceEntity existingEntity = deviceRepository.findById(dbId)
                    .orElseThrow(() -> exceptionFactory.createError(GenericException.class, 31, idStr));

            Map<String, Object> request = JsonDataExtractor.toMap(context.getPayload().getExtendedData());
            DeviceRegistrationRequest deviceRequest = JsonDataExtractor.fromJson(JsonDataExtractor.toJson(request), DeviceRegistrationRequest.class);

            DeviceEntity entityToUpdate = mapper.toEntity(deviceRequest);

            entityToUpdate.setId(existingEntity.getId());
            entityToUpdate.setDeviceId(existingEntity.getDeviceId());

            deviceRepository.save(entityToUpdate);

            tres.setStatus(PaymentStatus.SUCCESS);
            tres.setDescription("Device updated successfully");

            return createHttpResponse(tres);
        } catch (BaseException e) {
            tres.setStatus(String.valueOf(e.getErrorCode()));
            tres.setDescription(e.getMessage());
            return createHttpResponse(tres, e.getHttpStatus());
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
        return new HashMap<>();
    }
}