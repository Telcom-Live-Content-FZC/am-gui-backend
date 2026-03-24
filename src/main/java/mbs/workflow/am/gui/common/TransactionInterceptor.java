package mbs.workflow.am.gui.common;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class TransactionInterceptor {

    @Resource
    private SessionContext sessionContext;

    @AroundInvoke
    public Object handleTransaction(InvocationContext context) throws Exception {
        try {
            return context.proceed();
        } catch (Exception e) {
            if(log.isDebugEnabled() || log.isTraceEnabled()){
                log.error("Transaction Interceptor caught an unhandled exception: " + e.getMessage(), e);
            }
            if (!sessionContext.getRollbackOnly()) {
                sessionContext.setRollbackOnly();
                log.warn("Transaction marked for rollback due to: " + e.getClass().getSimpleName());
            }
            throw e;
        }
    }
}