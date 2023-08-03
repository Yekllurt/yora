package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.errors.ExecutionError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.interpreter.ast.ConditionOperator;
import dev.yekllurt.interpreter.interpreter.scope.FunctionScope;
import dev.yekllurt.interpreter.interpreter.scope.ParameterScope;
import dev.yekllurt.interpreter.interpreter.scope.ReturnScope;
import dev.yekllurt.interpreter.interpreter.scope.VariableScope;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.interpreter.utility.ParserUtility;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class ComparisonConditionNode implements ConditionNode {

    private final ASTNode left;
    private final ASTNode right;
    private final ConditionOperator operator;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        var returnScopeLeft = new ReturnScopeImplementation();
        var returnScopeRight = new ReturnScopeImplementation();
        left.evaluate(functionScope, variableScope, parameterScope, returnScopeLeft);
        right.evaluate(functionScope, variableScope, parameterScope, returnScopeRight);
        if (Objects.isNull(returnScopeLeft.lookupReturnValue()) && Objects.isNull(returnScopeRight.lookupReturnValue())) {
            ExceptionUtility.throwException(ExecutionError.INVALID_COMPARISON_BOTH_NULL);
        }
        switch (operator) {
            case EQUAL -> performEqual(returnScope, returnScopeLeft, returnScopeRight);
            case NOT_EQUAL -> performNotEqual(returnScope, returnScopeLeft, returnScopeRight);
            case GREATER_THAN -> performGreaterThan(returnScope, returnScopeLeft, returnScopeRight);
            case GREATER_THAN_EQUAL -> performGreaterThanEqual(returnScope, returnScopeLeft, returnScopeRight);
            case LESS_THAN -> performLessThan(returnScope, returnScopeLeft, returnScopeRight);
            case LESS_THAN_EQUAL -> performLessThanEqual(returnScope, returnScopeLeft, returnScopeRight);
            default ->
                    ExceptionUtility.throwException(ExecutionError.INVALID_COMPARISON_UNSUPPORTED_OPERATION, operator);
        }
    }

    // TODO: overhaul the perform methods so that they use the data functions
    private void performEqual(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        if (ParserUtility.isNumber(returnScopeLeft.lookupReturnValueType()) && ParserUtility.isNumber(returnScopeRight.lookupReturnValueType())) {
            var leftReturnValue = returnScopeLeft.lookup().toDouble();
            var rightReturnValue = returnScopeRight.lookup().toDouble();
            if (Objects.nonNull(leftReturnValue)) {
                var equal = leftReturnValue.compareTo(rightReturnValue) == 0;
                returnScope.assignReturnValue(DataType.BOOLEAN, equal);
            } else if (Objects.nonNull(rightReturnValue)) {
                var equal = rightReturnValue.compareTo(leftReturnValue) == 0;
                returnScope.assignReturnValue(DataType.BOOLEAN, equal);
            } else {
                // TODO: throw error
            }
        } else {
            var equal = Objects.isNull(returnScopeRight.lookupReturnValue())
                    ? returnScopeLeft.lookupReturnValue().equals(returnScopeRight.lookupReturnValue())
                    : returnScopeRight.lookupReturnValue().equals(returnScopeLeft.lookupReturnValue());
            returnScope.assignReturnValue(DataType.BOOLEAN, equal);
        }
    }

    private void performNotEqual(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        var equal = Objects.isNull(returnScopeRight.lookupReturnValue())
                ? returnScopeLeft.lookupReturnValue().equals(returnScopeRight.lookupReturnValue())
                : returnScopeRight.lookupReturnValue().equals(returnScopeLeft.lookupReturnValue());
        returnScope.assignReturnValue(DataType.BOOLEAN, !equal);
    }

    private void performGreaterThan(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        var greaterThan = compareTwoNumbers(returnScopeLeft.lookup(), returnScopeRight.lookup(), ">") > 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, greaterThan);
    }

    private void performGreaterThanEqual(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        var greaterThanEqual = compareTwoNumbers(returnScopeLeft.lookup(), returnScopeRight.lookup(), ">=") >= 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, greaterThanEqual);
    }

    private void performLessThan(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        var lessThan = compareTwoNumbers(returnScopeLeft.lookup(), returnScopeRight.lookup(), "<") < 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, lessThan);
    }

    private void performLessThanEqual(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        var lessThan = compareTwoNumbers(returnScopeLeft.lookup(), returnScopeRight.lookup(), "<=") <= 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, lessThan);
    }

    private int compareTwoNumbers(dev.yekllurt.interpreter.interpreter.scope.Data number1, dev.yekllurt.interpreter.interpreter.scope.Data number2, String comparison) {
        ExceptionUtility.throwExceptionIf(!number1.isNumber() || !number2.isNumber(),
                ExecutionError.INVALID_COMPARISON_NO_NUMBER,
                number1.dataType(), number2.dataType(), comparison);
        if (!number1.isDouble() || !number2.isDouble()) {
            return number1.toDouble().compareTo(number2.toDouble());
        }
        if (!number1.isLong() || !number2.isLong()) {
            return number1.toLong().compareTo(number2.toLong());
        }
        return 0;
    }

}
