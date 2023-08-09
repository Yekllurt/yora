package dev.yekllurt.api.throwable.execution;

/**
 * An exception that is thrown when the provided value is not of the expected type
 */
public class InvalidTypeException extends ExecutionException {

    public InvalidTypeException(String message) {
        super(message);
    }

}
