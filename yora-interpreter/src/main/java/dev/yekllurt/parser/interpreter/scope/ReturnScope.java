package dev.yekllurt.parser.interpreter.scope;

import dev.yekllurt.api.DataType;

public interface ReturnScope {

    /**
     * Set a return value in the current scope
     *
     * @param type  the return type
     * @param value the return value
     */
    void assignReturnValue(DataType type, Object value);

    Data lookup();

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
    DataType lookupReturnValueType();

    default String getScopeName() {
        return "return";
    }

}
