package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.errors.ScopeError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;

import java.util.Objects;

public class ReturnScopeImplementation implements ReturnScope {

    private Data data;

    @Override
    public void assignReturnValue(DataType type, Object value) {
        ExceptionUtility.throwExceptionIf(Objects.nonNull(this.data),
                ScopeError.CAN_NOT_ASSIGN_DATA_BECAUSE_IT_ALREADY_WAS_ASSIGNED,
                getScopeName());
        this.data = new Data(type, value);
    }

    @Override
    public Data lookup() {
        return this.data;
    }

    @Override
    public Object lookupReturnValue() {
        if (Objects.isNull(this.data)) {
            return null;
        }
        return this.data.data();
    }

    @Override
    public DataType lookupReturnValueType() {
        if (Objects.isNull(this.data)) {
            return null;
        }
        return this.data.dataType();
    }

}
