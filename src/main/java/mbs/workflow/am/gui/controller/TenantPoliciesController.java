package mbs.workflow.am.gui.controller;

import com.psi.mfsv4.mbs.common.http.HttpRequest;
import com.psi.mfsv4.mbs.common.http.HttpResponse;
import com.psi.mfsv4.mbs.common.interfaces.Workflow;
import mbs.workflow.am.gui.service.TenantPoliciesService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;


@Stateless
@Remote({ Workflow.class })
public class TenantPoliciesController implements Workflow {

    @EJB
    private TenantPoliciesService service;

    @Override
    public HttpResponse execute(HttpRequest request) {
        return service.executeWithLogging(request);
    }

    @Override
    public int getId() {
        return 0;
    }

}