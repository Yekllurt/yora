package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.throwable.ScopeError;

import java.util.Objects;

public class ReturnScopeImplementation implements ReturnScope {

    private Data data;

    @Override
    public void assignReturnValue(DataType type, Object value) {
        if (Objects.nonNull(this.data)) {
            throw new ScopeError("Can't assign the value '%s' as return value as in the scope a return valued has already been assigned");
        }
        this.data = new Data(type, value);
    }

    @Override
    public Data lookupReturnValue() {
        return this.data;
    }

}
