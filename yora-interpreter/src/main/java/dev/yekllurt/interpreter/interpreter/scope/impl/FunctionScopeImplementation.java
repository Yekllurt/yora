package dev.yekllurt.interpreter.interpreter.scope.impl;

import dev.yekllurt.api.errors.ScopeError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.interpreter.ast.impl.FunctionNode;
import dev.yekllurt.interpreter.interpreter.scope.FunctionScope;

import java.util.*;

public class FunctionScopeImplementation implements FunctionScope {

    private final Deque<Deque<Map<String, FunctionNode>>> functionScope = new ArrayDeque<>();

    @Override
    public void beginSoftScope() {
        ExceptionUtility.throwExceptionIf(functionScope.isEmpty() || Objects.isNull(functionScope.peek()),
                ScopeError.CAN_NOT_BEGIN_SOFT_SCOPE,
                getScopeName());

        functionScope.peek().push(new HashMap<>());
    }

    @Override
    public void endSoftScope() {
        ExceptionUtility.throwExceptionIf(functionScope.isEmpty() || Objects.isNull(functionScope.peek()) || functionScope.peek().isEmpty(),
                ScopeError.CAN_NOT_END_SOFT_SCOPE,
                getScopeName());

        functionScope.peek().pop();
    }

    @Override
    public void beginHardScope() {
        functionScope.push(new ArrayDeque<>());
    }

    @Override
    public void endHardScope() {
        ExceptionUtility.throwExceptionIf(functionScope.isEmpty(),
                ScopeError.CAN_NOT_END_HARD_SCOPE,
                getScopeName());

        functionScope.pop();
    }

    @Override
    public String getScopeName() {
        return "function";
    }

    @Override
    public void assignFunction(String name, FunctionNode function) {
        ExceptionUtility.throwExceptionIf(functionScope.isEmpty() || Objects.isNull(functionScope.peek()) || functionScope.peek().isEmpty(),
                ScopeError.CAN_NOT_ASSIGN_DATA_BECAUSE_NO_ACTIVE_SOFT_SCOPE,
                getScopeName(), name);
        ExceptionUtility.throwExceptionIf(existsFunction(name),
                ScopeError.CAN_NOT_ASSIGN_DATA_BECAUSE_IT_ALREADY_EXISTS_IN_SCOPE,
                getScopeName(), name);

        functionScope.peek().peek().put(name, function);
    }

    @Override
    public FunctionNode lookupFunction(String name) {
        ExceptionUtility.throwExceptionIf(functionScope.isEmpty() || Objects.isNull(functionScope.peek()) || functionScope.peek().isEmpty(),
                ScopeError.CAN_NOT_LOOKUP_DATA_BECAUSE_NO_ACTIVE_SCOPE,
                getScopeName(), name);

        for (var scope : functionScope.peek()) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }

        ExceptionUtility.throwException(ScopeError.CAN_NOT_FIND_DATA_IN_SCOPE,
                getScopeName(), name);
        return null;
    }

    private boolean existsFunction(String name) {
        ExceptionUtility.throwExceptionIf(functionScope.isEmpty() || Objects.isNull(functionScope.peek()) || functionScope.peek().isEmpty(),
                ScopeError.CAN_NOT_CHECK_EXISTENCE_BECAUSE_NO_ACTIVE_SCOPE,
                getScopeName(), name);

        for (var scope : functionScope.peek()) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

}

