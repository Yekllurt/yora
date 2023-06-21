package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.ConditionOperator;
import dev.yekllurt.parser.ast.Utility;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;
import dev.yekllurt.parser.interpreter.throwable.error.InvalidOperationError;
import dev.yekllurt.parser.token.TokenType;
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
            case EQUAL -> {
                var equal = Objects.isNull(returnScopeRight.lookupReturnValue())
                        ? returnScopeLeft.lookupReturnValue().equals(returnScopeRight.lookupReturnValue())
                        : returnScopeRight.lookupReturnValue().equals(returnScopeLeft.lookupReturnValue());
                returnScope.assignReturnValue(TokenType.KEYWORD_BOOLEAN, equal);
            }
            case NOT_EQUAL -> {
                var equal = Objects.isNull(returnScopeRight.lookupReturnValue())
                        ? returnScopeLeft.lookupReturnValue().equals(returnScopeRight.lookupReturnValue())
                        : returnScopeRight.lookupReturnValue().equals(returnScopeLeft.lookupReturnValue());
                returnScope.assignReturnValue(TokenType.KEYWORD_BOOLEAN, !equal);
            }
            case GREATER_THAN -> {
                if (!Utility.isNumber(returnScopeLeft.lookupReturnValue()) || !Utility.isNumber(returnScopeRight.lookupReturnValue())) {
                    throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the > operator however both must be numbers", returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
                }
                var greaterThan = Utility.parseDouble(returnScopeLeft.lookupReturnValue()) > Utility.parseDouble(returnScopeRight.lookupReturnValue());
                returnScope.assignReturnValue(TokenType.KEYWORD_BOOLEAN, greaterThan);
            }
            case GREATER_THAN_EQUAL -> {
                if (!Utility.isNumber(returnScopeLeft.lookupReturnValue()) || !Utility.isNumber(returnScopeRight.lookupReturnValue())) {
                    throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the >= operator however both must be numbers", returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
                }
                var greaterThanEqual = Utility.parseDouble(returnScopeLeft.lookupReturnValue()) >= Utility.parseDouble(returnScopeRight.lookupReturnValue());
                returnScope.assignReturnValue(TokenType.KEYWORD_BOOLEAN, greaterThanEqual);
            }
            case LESS_THAN -> {
                if (!Utility.isNumber(returnScopeLeft.lookupReturnValue()) || !Utility.isNumber(returnScopeRight.lookupReturnValue())) {
                    throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the < operator however both must be numbers", returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
                }
                var lessThan = Utility.parseDouble(returnScopeLeft.lookupReturnValue()) < Utility.parseDouble(returnScopeRight.lookupReturnValue());
                returnScope.assignReturnValue(TokenType.KEYWORD_BOOLEAN, lessThan);
            }
            case LESS_THAN_EQUAL -> {
                if (!Utility.isNumber(returnScopeLeft.lookupReturnValue()) || !Utility.isNumber(returnScopeRight.lookupReturnValue())) {
                    throw new InvalidOperationError(String.format("Attempting to compare two values of the type %s and %s using the <= operator however both must be numbers", returnScopeLeft.lookupReturnValueType(), returnScopeRight.lookupReturnValueType()));
                }
                var lessThan = Utility.parseDouble(returnScopeLeft.lookupReturnValue()) <= Utility.parseDouble(returnScopeRight.lookupReturnValue());
                returnScope.assignReturnValue(TokenType.KEYWORD_BOOLEAN, lessThan);
            }
            default ->
                    throw new UnsupportedOperationException("The condition operator '%s' is not supported".formatted(operator));
        }
    }

}
