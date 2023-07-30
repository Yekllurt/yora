package dev.yekllurt.api.throwable.execution;

/**
 * An error that is thrown when an invalid arithmetic condition occurred
 */
public class ArithmeticException extends ExecutionException {

    public ArithmeticException(String message) {
        super(message);
    }

}
