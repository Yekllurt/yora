package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.throwable.error.ScopeError;

import java.util.Objects;

public class ReturnScopeImplementation implements ReturnScope {

    private DataType type;
    private Object value;

    @Override
    public void assignReturnValue(DataType type, Object value) {
        if (Objects.nonNull(this.value)) {
            throw new ScopeError("Can't assign the value '%s' as return value as in the scope a return valued has already been assigned");
        }
        this.type = type;
        this.value = value;
    }

    @Override
    public Object lookupReturnValue() {
        return value;
    }

    @Override
    public DataType lookupReturnValueType() {
        return type;
    }

}
