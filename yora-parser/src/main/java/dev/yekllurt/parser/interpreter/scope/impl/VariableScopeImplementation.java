package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.throwable.error.ScopeError;
import dev.yekllurt.api.tuples.Tuple;

import java.util.*;

public class VariableScopeImplementation implements VariableScope {

    private final Deque<Deque<Map<String, Tuple<DataType, Object>>>> variableScope = new ArrayDeque<>();

    @Override
    public void beginSoftScope() {
        if (variableScope.isEmpty() || Objects.isNull(variableScope.peek())) {
            throw new ScopeError("Can't create a soft scope as no hard scope is active.");
        }
        variableScope.peek().push(new HashMap<>());
    }

    @Override
    public void endSoftScope() {
        if (variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty()) {
            throw new ScopeError("Can't end a soft scope as no hard scope is active.");
        }
        variableScope.peek().pop();
    }

    @Override
    public void beginHardScope() {
        variableScope.push(new ArrayDeque<>());
    }

    @Override
    public void endHardScope() {
        if (variableScope.isEmpty()) {
            throw new ScopeError("Can't end a hard scope as no scope is active.");
        }
        variableScope.pop();
    }

    @Override
    public void assignVariable(String name, DataType type, Object value) {
        if (variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty()) {
            throw new ScopeError("Can't assign the variable '%s' as there is not active soft scope available.".formatted(name));
        }
        if (existsVariable(name)) {
            throw new ScopeError(String.format("Can't assign the variable '%s' as it already exists in the current soft scope.", name));
        }
        variableScope.peek().peek().put(name, new Tuple<>(type, value));
    }

    @Override
    public void updateVariable(String name, Object value) {
        if (variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty()) {
            throw new ScopeError("Can't update the variable '%s' as there is no active soft scope available.".formatted(name));
        }
        if (!existsVariable(name)) {
            throw new ScopeError(String.format("Can't update the variable '%s' as it doesn't exist in the current soft scope.", name));
        }
        for (var scope : variableScope.peek()) {
            if (scope.containsKey(name)) {
                scope.put(name, new Tuple<>(lookupVariableType(name), value));
            }
        }
    }

    @Override
    public Object lookupVariable(String name) {
        if (variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty()) {
            throw new ScopeError(String.format("Can't lookup the variable '%s' as there is no active soft scope available.", name));
        }
        for (var scope : variableScope.peek()) {
            if (scope.containsKey(name)) {
                return scope.get(name).y();
            }
        }
        throw new ScopeError(String.format("Can't find the variable '%s' in the current scope or any parent soft scope.", name));
    }

    @Override
    public DataType lookupVariableType(String name) {
        if (variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty()) {
            throw new ScopeError(String.format("Can't lookup the variable '%s' as there is no active soft scope available.", name));
        }
        for (var scope : variableScope.peek()) {
            if (scope.containsKey(name)) {
                return scope.get(name).x();
            }
        }
        throw new ScopeError(String.format("Can't find the variable '%s' in the current soft scope or any parent scope.", name));
    }

    @Override
    public boolean existsVariable(String name) {
        if (variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty()) {
            throw new ScopeError(String.format("Can't check if the variable '%s' exists as there is no active soft scope available.", name));
        }
        for (var scope : variableScope.peek()) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

}

