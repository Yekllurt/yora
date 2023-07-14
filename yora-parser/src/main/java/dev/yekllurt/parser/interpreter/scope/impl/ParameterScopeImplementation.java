package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.throwable.ScopeError;

import java.util.HashMap;
import java.util.Map;

public class ParameterScopeImplementation implements ParameterScope {

    private final Map<String, Data> parameters = new HashMap<>();

    @Override
    public void assignData(String name, DataType type, Object value) {
        if (existsData(name)) {
            throw new ScopeError(String.format("Can't assign the parameter '%s' as it already exists in the current scope.", name));
        }
        parameters.put(name, new Data(type, value));
    }

    @Override
    public void updateData(String name, Object value) {
        if (!existsData(name)) {
            throw new ScopeError(String.format("Can't update the parameter '%s' as it doesn't exist in the current soft scope.", name));
        }
        parameters.put(name, new Data(lookupDataType(name), value));
    }

    @Override
    public Data lookup(String name) {
        if (!existsData(name)) {
            throw new ScopeError(String.format("Can't find the parameter '%s' in the current scope.", name));
        }
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
