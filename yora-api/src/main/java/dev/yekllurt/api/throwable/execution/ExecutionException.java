package dev.yekllurt.api.throwable.execution;

/**
 * An exception that is thrown during the execution of the program
 */
public class ExecutionException extends RuntimeException {

    public ExecutionException(String message) {
        super(message);
    }

}
