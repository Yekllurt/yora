package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.ConditionOperator;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;
import dev.yekllurt.parser.interpreter.throwable.error.InvalidOperationError;
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
            case EQUAL ->
                    performEqual(functionScope, variableScope, parameterScope, returnScope, returnScopeLeft, returnScopeRight);
            case NOT_EQUAL ->
                    performNotEqual(functionScope, variableScope, parameterScope, returnScope, returnScopeLeft, returnScopeRight);
            case GREATER_THAN ->
                    performGreaterThan(functionScope, variableScope, parameterScope, returnScope, returnScopeLeft, returnScopeRight);
            case GREATER_THAN_EQUAL ->
                    performGreaterThanEqual(functionScope, variableScope, parameterScope, returnScope, returnScopeLeft, returnScopeRight);
            case LESS_THAN ->
                    performLessThan(functionScope, variableScope, parameterScope, returnScope, returnScopeLeft, returnScopeRight);
            case LESS_THAN_EQUAL ->
                    performLessThanEqual(functionScope, variableScope, parameterScope, returnScope, returnScopeLeft, returnScopeRight);
            default ->
                    throw new UnsupportedOperationException("The condition operator '%s' is not supported".formatted(operator));
        }
    }

    private void performEqual(FunctionScope functionScope, VariableScope variableScope,
                              ParameterScope parameterScope, ReturnScope returnScope,
                              ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        if (ParserUtility.isNumber(returnScopeLeft.lookupReturnValue()) && ParserUtility.isNumber(returnScopeRight.lookupReturnValue())) {
            var leftReturnValue = ParserUtility.parseDouble(returnScopeLeft.lookupReturnValue());
            var rightReturnValue = ParserUtility.parseDouble(returnScopeRight.lookupReturnValue());
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

    private void performNotEqual(FunctionScope functionScope, VariableScope variableScope,
                                 ParameterScope parameterScope, ReturnScope returnScope,
                                 ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        var equal = Objects.isNull(returnScopeRight.lookupReturnValue())
                ? returnScopeLeft.lookupReturnValue().equals(returnScopeRight.lookupReturnValue())
                : returnScopeRight.lookupReturnValue().equals(returnScopeLeft.lookupReturnValue());
        returnScope.assignReturnValue(DataType.BOOLEAN, !equal);
    }

    private void performGreaterThan(FunctionScope functionScope, VariableScope variableScope,
                                    ParameterScope parameterScope, ReturnScope returnScope,
                                    ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        if (!ParserUtility.isNumber(returnScopeLeft.lookupReturnValue()) || !ParserUtility.isNumber(returnScopeRight.lookupReturnValue())) {
            throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the > operator however both must be numbers", returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
        }
        var greaterThan = ParserUtility.parseDouble(returnScopeLeft.lookupReturnValue()).compareTo(ParserUtility.parseDouble(returnScopeRight.lookupReturnValue())) > 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, greaterThan);
    }

    private void performGreaterThanEqual(FunctionScope functionScope, VariableScope variableScope,
                                         ParameterScope parameterScope, ReturnScope returnScope,
                                         ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        if (!ParserUtility.isNumber(returnScopeLeft.lookupReturnValue()) || !ParserUtility.isNumber(returnScopeRight.lookupReturnValue())) {
            throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the >= operator however both must be numbers", returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
        }
        var greaterThanEqual = ParserUtility.parseDouble(returnScopeLeft.lookupReturnValue()).compareTo(ParserUtility.parseDouble(returnScopeRight.lookupReturnValue())) >= 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, greaterThanEqual);
    }

    private void performLessThan(FunctionScope functionScope, VariableScope variableScope,
                                 ParameterScope parameterScope, ReturnScope returnScope,
                                 ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        if (!ParserUtility.isNumber(returnScopeLeft.lookupReturnValue()) || !ParserUtility.isNumber(returnScopeRight.lookupReturnValue())) {
            throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the < operator however both must be numbers", returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
        }
        var lessThan = ParserUtility.parseDouble(returnScopeLeft.lookupReturnValue()).compareTo(ParserUtility.parseDouble(returnScopeRight.lookupReturnValue())) < 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, lessThan);
    }

    private void performLessThanEqual(FunctionScope functionScope, VariableScope variableScope,
                                      ParameterScope parameterScope, ReturnScope returnScope,
                                      ReturnScope returnScopeLeft, ReturnScope returnScopeRight) {
        if (!ParserUtility.isNumber(returnScopeLeft.lookupReturnValue()) || !ParserUtility.isNumber(returnScopeRight.lookupReturnValue())) {
            throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the <= operator however both must be numbers", returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
        }
        var lessThan = ParserUtility.parseDouble(returnScopeLeft.lookupReturnValue()).compareTo(ParserUtility.parseDouble(returnScopeRight.lookupReturnValue())) <= 0;
        returnScope.assignReturnValue(DataType.BOOLEAN, lessThan);
    }

}
