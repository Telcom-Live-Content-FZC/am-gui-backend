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
import mbs.am.gui.dto.MobileAppInfoResponse;
import mbs.am.gui.entity.MobileAppInfoEntity;
import mbs.am.gui.mapper.MobileAppMapper;

import mbs.am.gui.repository.MobileAppRepository;
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
public class GetMobileAppInfoService extends AbstractRequest {

    @EJB
    private MobileAppRepository appRepo;

    @Inject
    private MobileAppMapper mapper;

    @Override
    public HttpResponse execute(RequestContext context) {
        TransactionResponse tres = new TransactionResponse();
        ExtendedData ext = new ExtendedData();

        tres.setRequestId(context.getPayload().getRequestId());
        tres.setTransactionId(context.getRequest().getHeader("referenceid"));
        try {
            String packageName = context.getRequest().getQueryParams("package-name");

            List<MobileAppInfoEntity> entities;

            if (packageName != null && !packageName.trim().isEmpty()) {
                entities = appRepo.findByPackageName(packageName);
            } else {
                entities = appRepo.findAll();
            }

            List<MobileAppInfoResponse> listOfMobileApps = entities.stream()
                    .map(mapper::toModel)
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());

            if (listOfMobileApps.isEmpty()) {
                tres.setStatus(String.valueOf(HttpStatus.SC_BAD_REQUEST));
                tres.setDescription("No data found");
                return createHttpResponse(tres, HttpStatus.SC_BAD_REQUEST);
            }

            ext.put("apps", listOfMobileApps);
            tres.setExtendedData(ext);
            tres.setStatus(PaymentStatus.SUCCESS);

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