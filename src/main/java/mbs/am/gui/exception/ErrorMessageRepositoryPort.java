package mbs.am.gui.exception;



import java.util.Optional;

public interface ErrorMessageRepositoryPort {

    Optional<ErrorMessage> findByCode(Long code);
}
