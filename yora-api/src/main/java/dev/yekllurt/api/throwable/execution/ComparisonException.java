package dev.yekllurt.api.throwable.execution;

/**
 * An error that is thrown when an invalid comparison occurred
 */
public class ComparisonException extends ExecutionException {

    public ComparisonException(String message) {
        super(message);
    }

}
