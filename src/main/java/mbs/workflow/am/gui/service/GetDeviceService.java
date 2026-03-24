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
import mbs.workflow.am.gui.entity.DeviceEntity;
import mbs.workflow.am.gui.mapper.DeviceMapper;
import mbs.workflow.am.gui.model.Device;
import mbs.workflow.am.gui.repository.DeviceRepository;
import org.apache.http.HttpStatus;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JBossLog
@Stateless
public class GetDeviceService extends AbstractRequest {

    @EJB
    private DeviceRepository deviceRepository;

    @Inject
    private DeviceMapper mapper;

    @Override
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));

        try {
            String deviceId = context.getRequest().getQueryParams("device-id");

            List<DeviceEntity> entities;

            if (deviceId != null && !deviceId.trim().isEmpty()) {
                entities = deviceRepository.findByDeviceId(deviceId)
                        .map(Collections::singletonList)
                        .orElseGet(Collections::emptyList);
            } else {
                entities = deviceRepository.findAll();
            }

            List<Device> models = entities.stream()
                    .map(mapper::toModel)
                    .collect(Collectors.toList());

            if (models.isEmpty()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_NOT_FOUND));
                tres.setDescription("Device not found.");
                return createHttpResponse(tres, HttpStatus.SC_NOT_FOUND);
            }

            ext.put("devices", models);
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
        return new HashMap<>();
    }
}