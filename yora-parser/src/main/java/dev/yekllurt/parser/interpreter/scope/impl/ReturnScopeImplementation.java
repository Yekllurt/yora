package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.parser.interpreter.scope.ReturnScope;

public class ReturnScopeImplementation implements ReturnScope {

    private String type;
    private Object value;

    @Override
    public void assignReturnValue(String type, Object value) {
        // TODO: throw error if it is already set
        this.type = type;
        this.value = value;
    }

    @Override
    public Object lookupReturnValue() {
        // TODO: throw error if it is not set
        return value;
    }

    @Override
    public String lookupReturnValueType() {
        // TODO: throw error if it is not set
        return type;
    }

}
