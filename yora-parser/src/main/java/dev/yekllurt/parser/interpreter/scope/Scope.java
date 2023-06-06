package dev.yekllurt.parser.interpreter.scope;

/**
 * An interface describing a scope
 */
public interface Scope {

    /**
     * Create a new scope
     */
    void beginScope();

    /**
     * Terminate the current scope
     */
    void endScope();

}
