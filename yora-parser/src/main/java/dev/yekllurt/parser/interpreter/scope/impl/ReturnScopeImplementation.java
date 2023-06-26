package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.throwable.error.ScopeError;

import java.util.Objects;

public class ReturnScopeImplementation implements ReturnScope {

    private String type;
    private Object value;

    @Override
    public void assignReturnValue(String type, Object value) {
        if (Objects.nonNull(this.value)) {
            throw new ScopeError("Can't assign the value '%s' as return value as in the scope a return valued has already been assigned");
        }
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
