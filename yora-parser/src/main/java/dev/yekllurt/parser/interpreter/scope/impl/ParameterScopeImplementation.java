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
    public void assignParameter(String name, DataType type, Object value) {
        if (existsParameter(name)) {
            throw new ScopeError(String.format("Can't assign the parameter '%s' as it already exists in the current scope.", name));
        }
        parameters.put(name, new Data(type, value));
    }

    @Override
    public void updateParameter(String name, Object value) {
        if (!existsParameter(name)) {
            throw new ScopeError(String.format("Can't update the parameter '%s' as it doesn't exist in the current soft scope.", name));
        }
        parameters.put(name, new Data(lookupParameterType(name), value));
    }

    @Override
    public Data lookup(String name) {
        if (!existsParameter(name)) {
            throw new ScopeError(String.format("Can't find the parameter '%s' in the current scope.", name));
        }
        return parameters.get(name);
    }

    @Override
    public Object lookupParameter(String name) {
        return lookup(name).data();
    }

    @Override
    public DataType lookupParameterType(String name) {
        return lookup(name).dataType();
    }

    @Override
    public boolean existsParameter(String name) {
        return parameters.containsKey(name);
    }

}
