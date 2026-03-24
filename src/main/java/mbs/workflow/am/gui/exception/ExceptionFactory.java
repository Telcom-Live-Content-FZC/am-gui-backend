package mbs.workflow.am.gui.exception;

import lombok.var;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ExceptionFactory {
    @Inject
    private MessageLookupService messageService;

    public <T extends BaseException> T createError(Class<T> exceptionClass, int code, Object... args) {
        var error = messageService.getFormattedError(code, args);
        try {
            return exceptionClass.getConstructor(int.class, String.class, int.class, Object[].class)
                    .newInstance(code, error.getMessage(), error.getHttpStatus(), args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create exception " + exceptionClass.getName(), e);
        }
    }
}
