package mbs.am.gui.exception;

public class GenericException extends BaseException {
    public GenericException(int errorCode, String message, int httpStatus, Object... args) {
        super(errorCode, message, httpStatus, args);
    }
}
