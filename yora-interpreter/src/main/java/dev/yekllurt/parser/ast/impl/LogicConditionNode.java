package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ConditionOperator;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.ExecutionError;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class LogicConditionNode implements ConditionNode {

    private final ConditionNode left;
    private final ConditionNode right;
    private final ConditionOperator operator;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        // TODO: add support that if the ConditionOperator is OR that we evaluate from left to right in order to improve performance,
        //  i.e. evaluate left check if true, if not check right
        var returnScopeLeft = new ReturnScopeImplementation();
        var returnScopeRight = new ReturnScopeImplementation();
        left.evaluate(functionScope, variableScope, parameterScope, returnScopeLeft);
        right.evaluate(functionScope, variableScope, parameterScope, returnScopeRight);
        if (Objects.isNull(returnScopeLeft.lookupReturnValue()) && Objects.isNull(returnScopeRight.lookupReturnValue())) {
            throw new ExecutionError("Couldn't compare two values as they are both null and null values are not supported by the language");
        }
        switch (operator) {
            case AND -> performAnd(returnScope, returnScopeLeft, returnScopeRight);
            case OR -> performOr(returnScope, returnScopeLeft, returnScopeRight);
            default ->
                    throw new UnsupportedOperationException("The condition operator '%s' is not supported".formatted(operator));
        }
    }

    private void performAnd(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        var leftData = returnScopeLeft.lookup();
        var rightData = returnScopeRight.lookup();
        if (DataType.BOOLEAN == leftData.dataType() && DataType.BOOLEAN == rightData.dataType()) {
            returnScope.assignReturnValue(DataType.BOOLEAN, leftData.toBoolean() == rightData.toBoolean());
        }
    }

    private void performOr(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        var leftData = returnScopeLeft.lookup();
        var rightData = returnScopeRight.lookup();
        if (DataType.BOOLEAN == leftData.dataType() && DataType.BOOLEAN == rightData.dataType()) {
            returnScope.assignReturnValue(DataType.BOOLEAN, leftData.toBoolean() || rightData.toBoolean());
        }
    }

}
