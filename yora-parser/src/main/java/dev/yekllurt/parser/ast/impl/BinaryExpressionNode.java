package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.utility.ParserUtility;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.error.InvalidOperationError;
import dev.yekllurt.parser.token.TokenType;
import dev.yekllurt.parser.utility.Tuple;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BinaryExpressionNode implements ASTNode {

    private final ASTNode left;
    private final ASTNode right;
    private final String operator;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        switch (operator) {
            case TokenType.PUNCTUATION_PLUS -> performPlus(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_MINUS -> performMinus(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_STAR -> performStar(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_DIVIDE ->
                    performDivide(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_CARET -> performPower(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_PERCENT ->
                    performPercent(functionScope, variableScope, parameterScope, returnScope);

            default -> throw new InvalidOperationError(String.format("No operation '%s' exists", operator));
        }
    }

    private void performPlus(FunctionScope functionScope, VariableScope variableScope,
                             ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValues.x()) && ParserUtility.isNumber(nodeValues.y())) {
            if (ParserUtility.isDouble(nodeValues.x()) || ParserUtility.isDouble(nodeValues.y())) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseDouble(nodeValues.x()) + ParserUtility.parseDouble(nodeValues.y()));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_INT, ParserUtility.parseLong(nodeValues.x()) + ParserUtility.parseLong(nodeValues.y()));
            }
        } else {
            returnScope.assignReturnValue(null, String.valueOf(nodeValues.x()) + String.valueOf(nodeValues.y()));
        }
    }

    private void performMinus(FunctionScope functionScope, VariableScope variableScope,
                              ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValues.x()) && ParserUtility.isNumber(nodeValues.y())) {
            if (ParserUtility.isDouble(nodeValues.x()) || ParserUtility.isDouble(nodeValues.y())) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseDouble(nodeValues.x()) - ParserUtility.parseDouble(nodeValues.y()));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_INT, ParserUtility.parseLong(nodeValues.x()) - ParserUtility.parseLong(nodeValues.y()));
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to subtract the values '%s' and '%s' with each other, both must be numbers", nodeValues.x().getClass().getSimpleName(), nodeValues.y().getClass().getSimpleName()));
        }
    }

    private void performStar(FunctionScope functionScope, VariableScope variableScope,
                             ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValues.x()) && ParserUtility.isNumber(nodeValues.y())) {
            if (ParserUtility.isDouble(nodeValues.x()) || ParserUtility.isDouble(nodeValues.y())) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseDouble(nodeValues.x()) * ParserUtility.parseDouble(nodeValues.y()));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_INT, ParserUtility.parseLong(nodeValues.x()) * ParserUtility.parseLong(nodeValues.y()));
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to multiply the values '%s' and '%s' with each other, both must be numbers", nodeValues.x().getClass().getSimpleName(), nodeValues.y().getClass().getSimpleName()));
        }
    }

    private void performDivide(FunctionScope functionScope, VariableScope variableScope,
                               ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValues.x()) && ParserUtility.isNumber(nodeValues.y())) {
            if (ParserUtility.parseDouble(nodeValues.y()) == 0) {
                throw new InvalidOperationError("Can't divide by 0");
            }
            if (ParserUtility.isDouble(nodeValues.x()) || ParserUtility.isDouble(nodeValues.y())) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseDouble(nodeValues.x()) / ParserUtility.parseDouble(nodeValues.y()));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseLong(nodeValues.x()) / ParserUtility.parseLong(nodeValues.y()));
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to divide the values '%s' and '%s' with each other, both must be numbers", nodeValues.x().getClass().getSimpleName(), nodeValues.y().getClass().getSimpleName()));
        }
    }

    private void performPower(FunctionScope functionScope, VariableScope variableScope,
                              ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValues.x()) && ParserUtility.isNumber(nodeValues.y())) {
            if (ParserUtility.isDouble(nodeValues.x()) || ParserUtility.isDouble(nodeValues.y())) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, Math.pow(ParserUtility.parseDouble(nodeValues.x()), ParserUtility.parseDouble(nodeValues.y())));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_INT, (int) Math.pow(ParserUtility.parseLong(nodeValues.x()), ParserUtility.parseLong(nodeValues.y())));
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to power the values '%s' and '%s' with each other, both must be numbers", nodeValues.x().getClass().getSimpleName(), nodeValues.y().getClass().getSimpleName()));
        }
    }

    private void performPercent(FunctionScope functionScope, VariableScope variableScope,
                                ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(functionScope, variableScope, parameterScope);
        if (ParserUtility.isLong(nodeValues.x()) && ParserUtility.isLong(nodeValues.y())) {
            returnScope.assignReturnValue(TokenType.KEYWORD_INT, ParserUtility.parseLong(nodeValues.x()) % ParserUtility.parseLong(nodeValues.y()));
        } else {
            throw new InvalidOperationError(String.format("Unable to modulo the values '%s' and '%s' with each other, both must be integers", nodeValues.x().getClass().getSimpleName(), nodeValues.y().getClass().getSimpleName()));
        }
    }

    private Tuple<Object, Object> getNodeValues(FunctionScope functionScope, VariableScope variableScope,
                                                ParameterScope parameterScope) {
        var returnScopeLeft = new ReturnScopeImplementation();
        left.evaluate(functionScope, variableScope, parameterScope, returnScopeLeft);
        var returnScopeRight = new ReturnScopeImplementation();
        right.evaluate(functionScope, variableScope, parameterScope, returnScopeRight);
        return new Tuple<>(returnScopeLeft.lookupReturnValue(), returnScopeRight.lookupReturnValue());
    }

}
