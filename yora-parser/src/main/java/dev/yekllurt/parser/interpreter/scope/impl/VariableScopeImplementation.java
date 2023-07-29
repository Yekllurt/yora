package dev.yekllurt.parser.interpreter.scope.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.errors.ScopeError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.interpreter.scope.VariableScope;

import java.util.*;

public class VariableScopeImplementation implements VariableScope {

    private final Deque<Deque<Map<String, Data>>> variableScope = new ArrayDeque<>();

    @Override
    public void beginSoftScope() {
        ExceptionUtility.throwExceptionIf(variableScope.isEmpty() || Objects.isNull(variableScope.peek()),
                ScopeError.CAN_NOT_BEGIN_SOFT_SCOPE,
                getScopeName());

        variableScope.peek().push(new HashMap<>());
    }

    @Override
    public void endSoftScope() {
        ExceptionUtility.throwExceptionIf(variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty(),
                ScopeError.CAN_NOT_END_SOFT_SCOPE,
                getScopeName());

        variableScope.peek().pop();
    }

    @Override
    public void beginHardScope() {
        variableScope.push(new ArrayDeque<>());
    }

    @Override
    public void endHardScope() {
        ExceptionUtility.throwExceptionIf(variableScope.isEmpty(),
                ScopeError.CAN_NOT_END_HARD_SCOPE,
                getScopeName());

        variableScope.pop();
    }

    @Override
    public String getScopeName() {
        return "variable";
    }

    @Override
    public void assignData(String name, DataType type, Object value) {
        ExceptionUtility.throwExceptionIf(variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty(),
                ScopeError.CAN_NOT_ASSIGN_DATA_BECAUSE_NO_ACTIVE_SOFT_SCOPE,
                getScopeName(), name);
        ExceptionUtility.throwExceptionIf(existsData(name),
                ScopeError.CAN_NOT_ASSIGN_DATA_BECAUSE_IT_ALREADY_EXISTS_IN_SCOPE,
                getScopeName(), name);

        variableScope.peek().peek().put(name, new Data(type, value));
    }

    @Override
    public void updateData(String name, Object value) {
        ExceptionUtility.throwExceptionIf(variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty(),
                ScopeError.CAN_NOT_UPDATE_DATA_BECAUSE_NO_ACTIVE_SOFT_SCOPE,
                getScopeName(), name);
        ExceptionUtility.throwExceptionIf(!existsData(name),
                ScopeError.CAN_NOT_UPDATE_DATA_BECAUSE_IT_DOES_NOT_EXISTS_IN_SCOPE,
                getScopeName(), name);

        for (var scope : variableScope.peek()) {
            if (scope.containsKey(name)) {
                scope.put(name, new Data(lookupDataType(name), value));
            }
        }
    }

    @Override
    public Data lookup(String name) {
        ExceptionUtility.throwExceptionIf(variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty(),
                ScopeError.CAN_NOT_LOOKUP_DATA_BECAUSE_NO_ACTIVE_SCOPE,
                getScopeName(), name);

        for (var scope : variableScope.peek()) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        ExceptionUtility.throwException(ScopeError.CAN_NOT_FIND_DATA_IN_SCOPE,
                getScopeName(), name);
        return null;
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
        ExceptionUtility.throwExceptionIf(variableScope.isEmpty() || Objects.isNull(variableScope.peek()) || variableScope.peek().isEmpty(),
                ScopeError.CAN_NOT_CHECK_EXISTENCE_BECAUSE_NO_ACTIVE_SCOPE,
                getScopeName(), name);

        for (var scope : variableScope.peek()) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

}

