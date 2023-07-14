package dev.yekllurt.parser.interpreter.scope;

import dev.yekllurt.api.DataType;

public interface VariableScope extends Scope {

    /**
     * Create a new variable in the current scope
     *
     * @param name  the variable name
     * @param type  the variable type
     * @param value the variable value
     */
    void assignVariable(String name, DataType type, Object value);

    /**
     * Updates a variable value in the current scope
     *
     * @param name  the variable name
     * @param value the variable value
     */
    void updateVariable(String name, Object value);

    Data lookup(String name);

    /**
     * Lookup a variable in the current scope
     *
     * @param name the variable name
     * @return the variable value
     */
    Object lookupVariable(String name);

    /**
     * Lookup a variable type in the current scope
     *
     * @param name the variable name
     * @return the variable type
     */
    DataType lookupVariableType(String name);

    /**
     * Check if a variable exists in the current scope
     *
     * @param name the variable name
     * @return if the variable exists in the current scope
     */
    boolean existsVariable(String name);

}
