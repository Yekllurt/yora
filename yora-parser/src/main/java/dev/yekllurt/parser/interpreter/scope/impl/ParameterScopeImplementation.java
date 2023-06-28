package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.throwable.error.ScopeError;
import dev.yekllurt.api.tuples.Tuple;

import java.util.HashMap;
import java.util.Map;

public class ParameterScopeImplementation implements ParameterScope {

    private final Map<String, Tuple<DataType, Object>> parameters = new HashMap<>();

    @Override
    public void assignParameter(String name, DataType type, Object value) {
        if (existsParameter(name)) {
            throw new ScopeError(String.format("Can't assign the parameter '%s' as it already exists in the current scope.", name));
        }
        parameters.put(name, new Tuple<>(type, value));
    }

    @Override
    public void updateParameter(String name, Object value) {
        if (!existsParameter(name)) {
            throw new ScopeError(String.format("Can't update the parameter '%s' as it doesn't exist in the current soft scope.", name));
        }
        parameters.put(name, new Tuple<>(lookupParameterType(name), value));
    }

    @Override
    public Object lookupParameter(String name) {
        if (!existsParameter(name)) {
            throw new ScopeError(String.format("Can't find the parameter '%s' in the current scope.", name));
        }
        return parameters.get(name).y();
    }

    @Override
    public DataType lookupParameterType(String name) {
        if (!existsParameter(name)) {
            throw new ScopeError(String.format("Can't find the parameter '%s' in the current scope.", name));
        }
        return parameters.get(name).x();
    }

    @Override
    public boolean existsParameter(String name) {
        return parameters.containsKey(name);
    }

}
