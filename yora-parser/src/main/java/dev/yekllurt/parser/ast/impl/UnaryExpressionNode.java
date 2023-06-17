package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.Utility;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.error.InvalidOperationError;
import dev.yekllurt.parser.token.TokenType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnaryExpressionNode implements ASTNode {

    private final ASTNode node;
    private final String operator;

    @Override
    public void evaluate(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        switch (operator) {
            case TokenType.PUNCTUATION_PLUS -> performPlus(variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_MINUS -> performMinus(variableScope, parameterScope, returnScope);

            default -> throw new InvalidOperationError(String.format("No operation '%s' exists", operator));
        }
    }

    private void performPlus(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValue = getNodeValue(variableScope, parameterScope);
        if (Utility.isNumber(nodeValue)) {
            if (Utility.isFloat(nodeValue)) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, nodeValue);
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_INT, nodeValue);
            }
        }
        // TODO: throw error
    }

    private void performMinus(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValue = getNodeValue(variableScope, parameterScope);
        if (Utility.isNumber(nodeValue)) {
            if (Utility.isFloat(nodeValue)) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, -Utility.parseFloat(nodeValue));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_INT, -Utility.parseInteger(nodeValue));
            }
        }
        // TODO: throw error
    }

    private Object getNodeValue(VariableScope variableScope, ParameterScope parameterScope) {
        var returnScope = new ReturnScopeImplementation();
        node.evaluate(variableScope, parameterScope, returnScope);
        return returnScope.lookupReturnValue();
    }

}