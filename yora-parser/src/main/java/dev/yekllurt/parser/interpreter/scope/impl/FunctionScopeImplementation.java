package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.parser.ast.impl.FunctionNode;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.throwable.error.ScopeError;

import java.util.*;

public class FunctionScopeImplementation implements FunctionScope {

    private final Deque<Map<String, FunctionNode>> functionScope = new ArrayDeque<>();

    @Override
    public void beginScope() {
        functionScope.push(new HashMap<>());
    }

    @Override
    public void endScope() {
        if (functionScope.isEmpty()) {
            throw new ScopeError("Can't end a scope as no scope is active.");
        }
        functionScope.pop();
    }

    @Override
    public void assignFunction(String name, FunctionNode function) {
        if (functionScope.isEmpty()) {
            throw new ScopeError("Can't assign the function '%s' as there is not active scope available.".formatted(name));
        }
        if (existsFunction(name)) {
            throw new ScopeError(String.format("Can't assign the function '%s' as it already exists in the current scope.", name));
        }
        functionScope.peek().put(name, function);
    }

    @Override
    public FunctionNode lookupFunction(String name) {
        if (functionScope.isEmpty()) {
            throw new ScopeError(String.format("Can't lookup the function '%s' as there is not active scope available.", name));
        }
        for (var scope : functionScope) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new ScopeError(String.format("Can't find the function '%s' in the current scope or any parent scope.", name));
    }

    private boolean existsFunction(String name) {
        if (functionScope.isEmpty()) {
            throw new ScopeError(String.format("Can't check if the function '%s' exists as there is not active scope available.", name));
        }
        for (var scope : functionScope) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

}

