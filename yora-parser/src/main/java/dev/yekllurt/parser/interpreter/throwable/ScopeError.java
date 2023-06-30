package dev.yekllurt.parser.interpreter.throwable;

/**
 * An error that is thrown when an invalid scope status is reached such as no active scope,
 * the requested variable not being available
 */
public class ScopeError extends Error {

    public ScopeError(String message) {
        super(message);
    }

}
