package dev.yekllurt.parser.interpreter.scope;

public interface ParameterScope {

    /**
     * Create a new parameter in the current scope
     *
     * @param name  the parameter name
     * @param type  the parameter type
     * @param value the parameter value
     */
    void assignParameter(String name, String type, Object value);

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
    String lookupParameterType(String name);

}
