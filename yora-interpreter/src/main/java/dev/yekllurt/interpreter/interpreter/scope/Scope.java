package dev.yekllurt.interpreter.interpreter.scope;

/**
 * An interface describing a scope <br>
 * A scope consists of one or more hard scopes containing each independently one or more soft scopes <br>
 */
public interface Scope {

    /**
     * Create a new soft scope
     */
    void beginSoftScope();

    /**
     * Terminate the current soft scope
     */
    void endSoftScope();

    /**
     * Create a new hard scope
     */
    void beginHardScope();

    /**
     * Terminate the current hard scope
     */
    void endHardScope();

    /**
     * @return the scope name
     */
    String getScopeName();

}
