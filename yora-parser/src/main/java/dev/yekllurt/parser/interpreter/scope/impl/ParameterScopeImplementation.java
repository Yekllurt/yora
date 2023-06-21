package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.throwable.error.ScopeError;
import dev.yekllurt.parser.utility.Tuple;

import java.util.HashMap;
import java.util.Map;

public class ParameterScopeImplementation implements ParameterScope {

    private final Map<String, Tuple<String, Object>> parameters = new HashMap<>();

    @Override
    public void assignParameter(String name, String type, Object value) {
        if (existsParameter(name)) {
            throw new ScopeError(String.format("Can't assign the parameter '%s' as it already exists in the current scope.", name));
        }
        parameters.put(name, new Tuple<>(type, value));
    }

    @Override
    public Object lookupParameter(String name) {
        if (!existsParameter(name)) {
            throw new ScopeError(String.format("Can't find the parameter '%s' in the current scope.", name));
        }
        return parameters.get(name).y();
    }

    @Override
    public String lookupParameterType(String name) {
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
