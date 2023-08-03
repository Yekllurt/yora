package dev.yekllurt.interpreter.interpreter.scope.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.errors.ScopeError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.interpreter.interpreter.scope.Data;
import dev.yekllurt.interpreter.interpreter.scope.ParameterScope;

import java.util.HashMap;
import java.util.Map;

public class ParameterScopeImplementation implements ParameterScope {

    private final Map<String, Data> parameters = new HashMap<>();

    @Override
    public void assignData(String name, DataType type, Object value) {
        ExceptionUtility.throwExceptionIf(existsData(name),
                ScopeError.CAN_NOT_ASSIGN_DATA_BECAUSE_IT_ALREADY_EXISTS_IN_SCOPE,
                getScopeName(), name);

        parameters.put(name, new Data(type, value));
    }

    @Override
    public void updateData(String name, Object value) {
        ExceptionUtility.throwExceptionIf(!existsData(name),
                ScopeError.CAN_NOT_UPDATE_DATA_BECAUSE_IT_DOES_NOT_EXISTS_IN_SCOPE,
                getScopeName(), name);

        parameters.put(name, new Data(lookupDataType(name), value));
    }

    @Override
    public Data lookup(String name) {
        ExceptionUtility.throwExceptionIf(!existsData(name),
                ScopeError.CAN_NOT_FIND_DATA_IN_SCOPE,
                getScopeName(), name);

        return parameters.get(name);
    }

    @Override
    public Object lookupData(String name) {
        return lookup(name).data();
    }

    @Override
    public DataType lookupDataType(String name) {
        return lookup(name).dataType();
    }

    @Override
    public boolean existsData(String name) {
        return parameters.containsKey(name);
    }

}
