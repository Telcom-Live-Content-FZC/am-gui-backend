package mbs.am.gui.controller;

import com.psi.mfsv4.mbs.common.http.HttpRequest;
import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.interfaces.Workflow;
import mbs.am.gui.service.MobileAppInfoUpdateService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;


@Stateless
@Remote({ Workflow.class })
public class MobileAppInfoUpdateController implements Workflow {

    @EJB
    private MobileAppInfoUpdateService service;

    @Override
    public HttpResponse execute(HttpRequest request) {
        return service.executeWithLogging(request);
    }

    @Override
    public int getId() {
        return 0;
    }

}