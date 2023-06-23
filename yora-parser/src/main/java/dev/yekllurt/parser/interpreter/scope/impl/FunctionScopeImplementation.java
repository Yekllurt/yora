package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.parser.ast.impl.FunctionNode;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.throwable.error.ScopeError;

import java.util.*;

public class FunctionScopeImplementation implements FunctionScope {

    private final Deque<Deque<Map<String, FunctionNode>>> functionScope = new ArrayDeque<>();

    @Override
    public void beginSoftScope() {
        if (functionScope.isEmpty() || Objects.isNull(functionScope.peek())) {
            throw new ScopeError("Can't create a soft scope as no hard scope is active.");
        }
        functionScope.peek().push(new HashMap<>());
    }

    @Override
    public void endSoftScope() {
        if (functionScope.isEmpty() || Objects.isNull(functionScope.peek()) || functionScope.peek().isEmpty()) {
            throw new ScopeError("Can't end a soft scope as no hard scope is active.");
        }
        functionScope.peek().pop();
    }

    @Override
    public void beginHardScope() {
        functionScope.push(new ArrayDeque<>());
    }

    @Override
    public void endHardScope() {
        if (functionScope.isEmpty()) {
            throw new ScopeError("Can't end a hard scope as no scope is active.");
        }
        functionScope.pop();
    }

    @Override
    public void assignFunction(String name, FunctionNode function) {
        if (functionScope.isEmpty() || Objects.isNull(functionScope.peek()) || functionScope.peek().isEmpty()) {
            throw new ScopeError("Can't assign the function '%s' as there is not active soft scope available.".formatted(name));
        }
        if (existsFunction(name)) {
            throw new ScopeError(String.format("Can't assign the function '%s' as it already exists in the current soft scope.", name));
        }
        functionScope.peek().peek().put(name, function);
    }

    @Override
    public FunctionNode lookupFunction(String name) {
        if (functionScope.isEmpty() || Objects.isNull(functionScope.peek()) || functionScope.peek().isEmpty()) {
            throw new ScopeError(String.format("Can't lookup the function '%s' as there is not active soft scope available.", name));
        }
        for (var scope : functionScope.peek()) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new ScopeError(String.format("Can't find the function '%s' in the current scope or any parent scope.", name));
    }

    private boolean existsFunction(String name) {
        if (functionScope.isEmpty() || Objects.isNull(functionScope.peek()) || functionScope.peek().isEmpty()) {
            throw new ScopeError(String.format("Can't check if the function '%s' exists as there is not active scope available.", name));
        }
        for (var scope : functionScope.peek()) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

}

