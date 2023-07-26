package dev.yekllurt.api.throwable.execution;

/**
 * An exception that is thrown when calling a function with an invalid argument count
 */
public class ArgumentCountMismatchException extends ExecutionException {

    public ArgumentCountMismatchException(String message) {
        super(message);
    }

}
