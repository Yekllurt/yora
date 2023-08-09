package dev.yekllurt.api.throwable.execution;

/**
 * An error that is thrown when parsing invalid data
 */
public class ParseException extends ExecutionException {

    public ParseException(String message) {
        super(message);
    }

}
