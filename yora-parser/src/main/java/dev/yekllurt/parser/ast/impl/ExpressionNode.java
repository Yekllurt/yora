package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.ParserUtility;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;
import dev.yekllurt.parser.interpreter.throwable.error.InvalidOperationError;
import dev.yekllurt.parser.token.TokenType;
import dev.yekllurt.parser.utility.Tuple;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpressionNode implements ASTNode {

    private final ASTNode left;             // is normally always a TermNode
    private final ASTNode right;            // is normally always a TermNode or ExpressionNode
    private final String operator;

    @Override
    public void evaluate(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        assertNodeType();

        switch (operator) {
            case TokenType.PUNCTUATION_PLUS -> performPlus(variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_MINUS -> performMinus(variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_STAR -> performStar(variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_DIVIDE -> performDivide(variableScope, parameterScope, returnScope);

            default -> throw new InvalidOperationError(String.format("No operation '%s' exists", operator));
        }
    }

    private void assertNodeType() {
        if (!(left instanceof TermNode)) {
            throw new ExecutionError(String.format("The left node of an expression node must always be a TermNode, however it was of type '%s'", left.getClass().getSimpleName()));
        }
        if (!((right instanceof TermNode) || (right instanceof ExpressionNode))) {
            throw new ExecutionError(String.format("The left node of an expression node must always be a TermNode or an ExpressionNode, however it was of type '%s'", left.getClass().getSimpleName()));
        }
    }

    private void performPlus(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValues.x()) && ParserUtility.isNumber(nodeValues.y())) {
            if (ParserUtility.isFloat(nodeValues.x()) || ParserUtility.isFloat(nodeValues.y())) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseFloat(nodeValues.x()) + ParserUtility.parseFloat(nodeValues.y()));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseInteger(nodeValues.x()) + ParserUtility.parseInteger(nodeValues.y()));
            }
        } else {
            returnScope.assignReturnValue(null, String.valueOf(nodeValues.x()) + String.valueOf(nodeValues.y()));
        }
    }

    private void performMinus(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValues.x()) && ParserUtility.isNumber(nodeValues.y())) {
            if (ParserUtility.isFloat(nodeValues.x()) || ParserUtility.isFloat(nodeValues.y())) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseFloat(nodeValues.x()) - ParserUtility.parseFloat(nodeValues.y()));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseInteger(nodeValues.x()) - ParserUtility.parseInteger(nodeValues.y()));
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to subtract the values '%s' and '%s' with each other, both must be numbers", nodeValues.x().getClass().getSimpleName(), nodeValues.y().getClass().getSimpleName()));
        }
    }

    private void performStar(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValues.x()) && ParserUtility.isNumber(nodeValues.y())) {
            if (ParserUtility.isFloat(nodeValues.x()) || ParserUtility.isFloat(nodeValues.y())) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseFloat(nodeValues.x()) * ParserUtility.parseFloat(nodeValues.y()));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseInteger(nodeValues.x()) * ParserUtility.parseInteger(nodeValues.y()));
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to multiply the values '%s' and '%s' with each other, both must be numbers", nodeValues.x().getClass().getSimpleName(), nodeValues.y().getClass().getSimpleName()));
        }
    }

    private void performDivide(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValues = getNodeValues(variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValues.x()) && ParserUtility.isNumber(nodeValues.y())) {
            if (ParserUtility.parseFloat(nodeValues.y()) == 0) {
                throw new InvalidOperationError("Can't divide by 0");
            }
            if (ParserUtility.isFloat(nodeValues.x()) || ParserUtility.isFloat(nodeValues.y())) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseFloat(nodeValues.x()) / ParserUtility.parseFloat(nodeValues.y()));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, ParserUtility.parseInteger(nodeValues.x()) / ParserUtility.parseInteger(nodeValues.y()));
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to divide the values '%s' and '%s' with each other, both must be numbers", nodeValues.x().getClass().getSimpleName(), nodeValues.y().getClass().getSimpleName()));
        }
    }

    private Tuple<Object, Object> getNodeValues(VariableScope variableScope, ParameterScope parameterScope) {
        var returnScopeLeft = new ReturnScopeImplementation();
        left.evaluate(variableScope, parameterScope, returnScopeLeft);
        var returnScopeRight = new ReturnScopeImplementation();
        right.evaluate(variableScope, parameterScope, returnScopeRight);
        return new Tuple<>(returnScopeLeft.lookupReturnValue(), returnScopeRight.lookupReturnValue());
    }

}
