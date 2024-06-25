package org.zeasn.exceptions;

public class IdTokenException extends Exception{
    public IdTokenException() {
        super();
    }

    public IdTokenException(String message) {
        super(message);
    }

    public IdTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdTokenException(Throwable cause) {
        super(cause);
    }

    protected IdTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
