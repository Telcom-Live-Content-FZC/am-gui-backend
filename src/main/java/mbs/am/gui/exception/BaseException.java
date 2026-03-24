package mbs.am.gui.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final int errorCode;
    private final int httpStatus;
    private final Object[] args;

    public BaseException(int errorCode, String message, int httpStatus, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }
}
