package dev.yekllurt.parser.interpreter.scope;

import dev.yekllurt.parser.interpreter.throwable.error.ScopeError;
import dev.yekllurt.parser.utility.Tuple;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ScopeImplementation implements VariableScope {

    private final Deque<Map<String, Tuple<String, Object>>> scopeVariableStack = new ArrayDeque<>();

    @Override
    public void beginScope() {
        scopeVariableStack.push(new HashMap<>());
    }

    @Override
    public void endScope() {
        if (scopeVariableStack.isEmpty()) {
            throw new ScopeError("Can't end a scope as no scope is active.");
        }
        scopeVariableStack.pop();
    }

    @Override
    public void assignVariable(String name, String type, Object value) {
        if (scopeVariableStack.isEmpty()) {
            throw new ScopeError("Can't assign the variable '%s' as there is not active scope available.".formatted(name));
        }
        if (exists(name)) {
            throw new ScopeError(String.format("Can't assign the variable '%s' as it already exists in the current scope.", name));
        }
        scopeVariableStack.peek().put(name, new Tuple<>(type, value));
    }

    @Override
    public void updateVariable(String name, Object value) {
        if (scopeVariableStack.isEmpty()) {
            throw new ScopeError("Can't update the variable '%s' as there is not active scope available.".formatted(name));
        }
        if (!exists(name)) {
            throw new ScopeError(String.format("Can't update the variable '%s' as it doesn't exist in the current scope.", name));
        }
        for (var scope : scopeVariableStack) {
            if (scope.containsKey(name)) {
                scope.put(name, new Tuple<>(lookupVariableType(name), value));
            }
        }
    }

    @Override
    public Object lookupVariable(String name) {
        if (scopeVariableStack.isEmpty()) {
            throw new ScopeError(String.format("Can't lookup the variable '%s' as there is not active scope available.", name));
        }
        for (var scope : scopeVariableStack) {
            if (scope.containsKey(name)) {
                return scope.get(name).y();
            }
        }
        throw new ScopeError(String.format("Can't find the variable '%s' in the current scope or any parent scope.", name));
    }

    @Override
    public String lookupVariableType(String name) {
        if (scopeVariableStack.isEmpty()) {
            throw new ScopeError(String.format("Can't lookup the variable '%s' as there is not active scope available.", name));
        }
        for (var scope : scopeVariableStack) {
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
    private boolean exists(String name) {
        if (scopeVariableStack.isEmpty()) {
            throw new ScopeError(String.format("Can't check if the variable '%s' exists as there is not active scope available.", name));
        }
        for (var scope : scopeVariableStack) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

}

