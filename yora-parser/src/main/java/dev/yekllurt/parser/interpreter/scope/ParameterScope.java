package dev.yekllurt.parser.interpreter.scope;

import dev.yekllurt.api.DataType;

public interface ParameterScope {

    /**
     * Create a new parameter in the current scope
     *
     * @param name  the parameter name
     * @param type  the parameter type
     * @param value the parameter value
     */
    void assignParameter(String name, DataType type, Object value);

    /**
     * Updates a parameter value in the current scope
     *
     * @param name  the variable name
     * @param value the variable value
     */
    void updateParameter(String name, Object value);

    /**
     * Lookup a parameter in the current scope
     *
     * @param name the parameter name
     * @return the parameter value
     */
    Object lookupParameter(String name);

    /**
     * Lookup a parameter type in the current scope
     *
     * @param name the parameter name
     * @return the parameter type
     */
    DataType lookupParameterType(String name);

    /**
     * Check if a parameter exists in the current scope
     *
     * @param name the parameter name
     * @return if the parameter exists in the current scope
     */
    boolean existsParameter(String name);

}
