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
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnaryExpressionNode implements ASTNode {

    private final ASTNode node;
    private final String operator;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        switch (operator) {
            case TokenType.PUNCTUATION_PLUS -> performPlus(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_MINUS -> performMinus(functionScope, variableScope, parameterScope, returnScope);

            default -> throw new InvalidOperationError(String.format("No operation '%s' exists", operator));
        }
    }

    private void performPlus(FunctionScope functionScope, VariableScope variableScope,
                             ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValue = getNodeValue(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValue)) {
            if (ParserUtility.isDouble(nodeValue)) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, nodeValue);
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_INT, nodeValue);
            }
        }
        // TODO: throw error
    }

    private void performMinus(FunctionScope functionScope, VariableScope variableScope,
                              ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeValue = getNodeValue(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeValue)) {
            if (ParserUtility.isDouble(nodeValue)) {
                returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, -ParserUtility.parseDouble(nodeValue));
            } else {
                returnScope.assignReturnValue(TokenType.KEYWORD_INT, -ParserUtility.parseLong(nodeValue));
            }
        }
        // TODO: throw error
    }

    private Object getNodeValue(FunctionScope functionScope, VariableScope variableScope,
                                ParameterScope parameterScope) {
        var returnScope = new ReturnScopeImplementation();
        node.evaluate(functionScope, variableScope, parameterScope, returnScope);
        return returnScope.lookupReturnValue();
    }

}