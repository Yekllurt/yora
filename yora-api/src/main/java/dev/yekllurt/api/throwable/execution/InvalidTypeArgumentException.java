package dev.yekllurt.api.throwable.execution;

/**
 * An exception that is thrown when calling a function and the provided value is not of the expected type
 */
public class InvalidTypeArgumentException extends ExecutionException {

    public InvalidTypeArgumentException(String message) {
        super(message);
    }

}
