package cs455.scaling.exception;


/**
 * handle possible exceptions thrown
 */
public class MessageProcessingException extends Exception {
    public MessageProcessingException() {
        super();
    }

    public MessageProcessingException(String message) {
        super(message);
    }

    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageProcessingException(Throwable cause) {
        super(cause);
    }

    protected MessageProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
