package mbs.workflow.am.gui.exception;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class ErrorMessagePersistenceAdapter implements ErrorMessageRepositoryPort {

    @Inject
    private MessageLookupService messageService;

    @Override
    public Optional<ErrorMessage> findByCode(Long code) {
        ErrorMessageEntity entity = messageService.getError(code);
        return Optional.of(ErrorMessage.builder()
                .code(entity.getCode())
                .message(entity.getMessage())
                .httpStatus(entity.getHttpStatus())
                .build());
    }
}