package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.throwable.error.ScopeError;
import dev.yekllurt.parser.utility.Tuple;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class VariableScopeImplementation implements VariableScope {

    private final Deque<Map<String, Tuple<String, Object>>> variableScope = new ArrayDeque<>();

    @Override
    public void beginScope() {
        variableScope.push(new HashMap<>());
    }

    @Override
    public void endScope() {
        if (variableScope.isEmpty()) {
            throw new ScopeError("Can't end a scope as no scope is active.");
        }
        variableScope.pop();
    }

    @Override
    public void assignVariable(String name, String type, Object value) {
        if (variableScope.isEmpty()) {
            throw new ScopeError("Can't assign the variable '%s' as there is not active scope available.".formatted(name));
        }
        if (existsVariable(name)) {
            throw new ScopeError(String.format("Can't assign the variable '%s' as it already exists in the current scope.", name));
        }
        variableScope.peek().put(name, new Tuple<>(type, value));
    }

    @Override
    public void updateVariable(String name, Object value) {
        if (variableScope.isEmpty()) {
            throw new ScopeError("Can't update the variable '%s' as there is not active scope available.".formatted(name));
        }
        if (!existsVariable(name)) {
            throw new ScopeError(String.format("Can't update the variable '%s' as it doesn't exist in the current scope.", name));
        }
        for (var scope : variableScope) {
            scope.computeIfPresent(name, (key, val) -> scope.put(name, new Tuple<>(lookupVariableType(name), value)));
        }
    }

    @Override
    public Object lookupVariable(String name) {
        if (variableScope.isEmpty()) {
            throw new ScopeError(String.format("Can't lookup the variable '%s' as there is not active scope available.", name));
        }
        for (var scope : variableScope) {
            if (scope.containsKey(name)) {
                return scope.get(name).y();
            }
        }
        throw new ScopeError(String.format("Can't find the variable '%s' in the current scope or any parent scope.", name));
    }

    @Override
    public String lookupVariableType(String name) {
        if (variableScope.isEmpty()) {
            throw new ScopeError(String.format("Can't lookup the variable '%s' as there is not active scope available.", name));
        }
        for (var scope : variableScope) {
            if (scope.containsKey(name)) {
                return scope.get(name).x();
            }
        }
        throw new ScopeError(String.format("Can't find the variable '%s' in the current scope or any parent scope.", name));
    }

    /**
     * Check if a variable exists in the current scope
     *
     * @param name the variable name
     * @return if the variable exists in the current scope
     */
    private boolean existsVariable(String name) {
        if (variableScope.isEmpty()) {
            throw new ScopeError(String.format("Can't check if the variable '%s' exists as there is not active scope available.", name));
        }
        for (var scope : variableScope) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

}

