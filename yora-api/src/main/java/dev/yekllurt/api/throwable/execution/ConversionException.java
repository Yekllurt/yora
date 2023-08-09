package dev.yekllurt.api.throwable.execution;

/**
 * An error that is thrown when a conversion is performed
 */
public class ConversionException extends ExecutionException {

    public ConversionException(String message) {
        super(message);
    }

}
