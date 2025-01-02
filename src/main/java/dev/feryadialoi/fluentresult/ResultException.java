package dev.feryadialoi.fluentresult;

public class ResultException extends RuntimeException {
    public ResultException() {
        super();
    }

    public ResultException(String message) {
        super(message);
    }

    public ResultException(Throwable cause) {
        super(cause);
    }

    public ResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
