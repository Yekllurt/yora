package dev.yekllurt.parser.interpreter.scope;

public interface ReturnScope {

    /**
     * Set a return value in the current scope
     *
     * @param type  the return type
     * @param value the return value
     */
    void assignReturnValue(String type, Object value);

    /**
     * Lookup the return value in the current scope
     *
     * @return the return value
     */
    Object lookupReturnValue();

    /**
     * Lookup the type of the return value
     *
     * @return the return type
     */
    String lookupReturnValueType();

}
