package mbs.workflow.am.gui.exception;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class MessageLookupService {

    @EJB
    private ErrorMessageRepository repository;
    private final Map<Long, ErrorMessageEntity> cache = new ConcurrentHashMap<>();

    public ErrorMessageEntity getError(Long code) {
        return cache.computeIfAbsent(code, k ->
                repository.findById(k)
                        .orElse(new ErrorMessageEntity(k,"E", "Unexpected Error", 500,1))
        );
    }
    public ErrorMessageEntity getFormattedError(int code, Object... args) {
        ErrorMessageEntity entity = getError(Long.valueOf(code));
        if (args != null && args.length > 0 && entity.getMessage() != null && entity.getMessage().contains("%s")) {
            try {
                String formatted = String.format(entity.getMessage(), args);
                return new ErrorMessageEntity(code,entity.getLanguage(), formatted, entity.getHttpStatus(), entity.getStatus());
            } catch (Exception e) {
                return entity;
            }
        }
        return entity;
    }
    public void clearCache() {
        cache.clear();
    }

}
