package mbs.am.gui.service;

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
import mbs.am.gui.common.TransactionInterceptor;
import mbs.am.gui.dto.MobileAppInfoRequest;
import mbs.am.gui.entity.MobileAppInfoEntity;
import mbs.am.gui.mapper.MobileAppMapper;
import mbs.am.gui.model.MobileAppInfo;
import mbs.am.gui.repository.MobileAppRepository;
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
public class MobileAppInfoUpdateService extends AbstractRequest {

    @EJB
    private MobileAppRepository appRepo;

    @Inject
    private MobileAppMapper mapper;

    @Override
    @Interceptors(TransactionInterceptor.class)
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));
        try {
            String appIdStr = context.getRequest().getPathParams().get(1);

            if (appIdStr == null || appIdStr.trim().isEmpty()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_BAD_REQUEST));
                tres.setDescription("App-id is required");
                return createHttpResponse(tres, HttpStatus.SC_BAD_REQUEST);
            }

            long appId = Long.parseLong(appIdStr);

            Optional<MobileAppInfoEntity> existingEntity = appRepo.findById(appId);
            if (!existingEntity.isPresent()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_NOT_FOUND));
                tres.setDescription("Mobile App Info not found for the provided ID.");
                return createHttpResponse(tres, HttpStatus.SC_NOT_FOUND);
            }

            Map<String, Object> request = JsonDataExtractor.toMap(context.getPayload().getExtendedData());
            MobileAppInfoRequest mobileAppInfoRequest = JsonDataExtractor.fromJson(JsonDataExtractor.toJson(request), MobileAppInfoRequest.class);

            MobileAppInfo model = mapper.toModel(mobileAppInfoRequest);
            model.setId(appId);

            MobileAppInfoEntity entityToUpdate = mapper.toEntity(model);
            MobileAppInfoEntity savedEntity = appRepo.save(entityToUpdate);

            MobileAppInfo savedModel = mapper.toModel(savedEntity);
            ext.put("app-info", mapper.toResponse(savedModel));

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