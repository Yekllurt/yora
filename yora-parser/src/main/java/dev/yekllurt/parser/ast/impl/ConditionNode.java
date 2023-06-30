package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.ConditionOperator;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.ExecutionError;
import dev.yekllurt.parser.interpreter.throwable.InvalidOperationError;
import dev.yekllurt.parser.utility.ParserUtility;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class ConditionNode implements ASTNode {

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
            throw new ExecutionError("Couldn't compare two values as they are both null and null values are not supported by the language");
        }
        switch (operator) {
            case EQUAL -> performEqual(returnScope, returnScopeLeft, returnScopeRight);
            case NOT_EQUAL -> performNotEqual(returnScope, returnScopeLeft, returnScopeRight);
            case GREATER_THAN -> performGreaterThan(returnScope, returnScopeLeft, returnScopeRight);
            case GREATER_THAN_EQUAL -> performGreaterThanEqual(returnScope, returnScopeLeft, returnScopeRight);
            case LESS_THAN -> performLessThan(returnScope, returnScopeLeft, returnScopeRight);
            case LESS_THAN_EQUAL -> performLessThanEqual(returnScope, returnScopeLeft, returnScopeRight);
            default ->
                    throw new UnsupportedOperationException("The condition operator '%s' is not supported".formatted(operator));
        }
    }

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
        if (!ParserUtility.isNumber(returnScopeLeft.lookupReturnValueType()) || !ParserUtility.isNumber(returnScopeRight.lookupReturnValueType())) {
            throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the > operator however both must be numbers",
                    returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
        }
        var greaterThan = compareTwoNumbers(returnScopeLeft.lookup(), returnScopeRight.lookup()) > 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, greaterThan);
    }

    private void performGreaterThanEqual(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        if (!ParserUtility.isNumber(returnScopeLeft.lookupReturnValueType()) || !ParserUtility.isNumber(returnScopeRight.lookupReturnValueType())) {
            throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the >= operator however both must be numbers",
                    returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
        }
        var greaterThanEqual = compareTwoNumbers(returnScopeLeft.lookup(), returnScopeRight.lookup()) >= 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, greaterThanEqual);
    }

    private void performLessThan(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        if (!ParserUtility.isNumber(returnScopeLeft.lookupReturnValueType()) || !ParserUtility.isNumber(returnScopeRight.lookupReturnValueType())) {
            throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the < operator however both must be numbers",
                    returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
        }
        var lessThan = compareTwoNumbers(returnScopeLeft.lookup(), returnScopeRight.lookup()) < 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, lessThan);
    }

    private void performLessThanEqual(ReturnScope returnScope, ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        if (!ParserUtility.isNumber(returnScopeLeft.lookupReturnValueType()) || !ParserUtility.isNumber(returnScopeRight.lookupReturnValueType())) {
            throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the <= operator however both must be numbers",
                    returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
        }
        var lessThan = compareTwoNumbers(returnScopeLeft.lookup(), returnScopeRight.lookup()) <= 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, lessThan);
    }

    private int compareTwoNumbers(dev.yekllurt.parser.interpreter.scope.Data number1, dev.yekllurt.parser.interpreter.scope.Data number2) {
        if (!number1.isDouble() || !number2.isDouble()) {
            return number1.toDouble().compareTo(number2.toDouble());
        }
        if (!number1.isLong() || !number2.isLong()) {
            return number1.toLong().compareTo(number2.toLong());
        }
        throw new ExecutionError("Attempting two compare two non numbers with each other that are supposed to be numbers");
    }

}
