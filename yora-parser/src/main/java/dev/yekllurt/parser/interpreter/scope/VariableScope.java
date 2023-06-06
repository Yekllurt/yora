package dev.yekllurt.parser.interpreter.scope;

public interface VariableScope extends Scope {

    /**
     * Create a new variable in the current scope
     *
     * @param name  the variable name
     * @param type  the variable type
     * @param value the variable value
     */
    void assignVariable(String name, String type, Object value);

    /**
     * Updates a variable value in the current scope
     *
     * @param name  the variable name
     * @param value the variable value
     */
    void updateVariable(String name, Object value);

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
    String lookupVariableType(String name);

}
