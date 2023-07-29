package dev.yekllurt.api.throwable.execution;

/**
 * An error that is thrown when an invalid scope status is reached such as no active scope,
 * the requested variable not being available
 */
public class ScopeException extends ExecutionException {

    public ScopeException(String message) {
        super(message);
    }

}
