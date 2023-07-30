package dev.yekllurt.api.throwable.execution;

/**
 * An error that is thrown when an invalid variable assignment/update occurs
 */
public class VariableException extends ExecutionException {

    public VariableException(String message) {
        super(message);
    }

}
