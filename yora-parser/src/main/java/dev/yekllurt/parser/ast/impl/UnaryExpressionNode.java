package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.InvalidOperationError;
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
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (nodeData.isNumber()) {
            if (nodeData.isDouble()) {
                returnScope.assignReturnValue(nodeData.dataType(), nodeData.data());
                return;
            } else {
                returnScope.assignReturnValue(nodeData.dataType(), nodeData.data());
                return;
            }
        }
        throw new InvalidOperationError(String.format("Can't perform a unary plus operation on data of the data type %s", nodeData.dataType()));
    }

    private void performMinus(FunctionScope functionScope, VariableScope variableScope,
                              ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (nodeData.isNumber()) {
            if (nodeData.isDouble()) {
                returnScope.assignReturnValue(nodeData.dataType(), -nodeData.toDouble());
                return;
            } else {
                returnScope.assignReturnValue(nodeData.dataType(), -nodeData.toLong());
                return;
            }
        }
        throw new InvalidOperationError(String.format("Can't perform a unary minus operation on data of the data type %s", nodeData.dataType()));
    }

    private dev.yekllurt.parser.interpreter.scope.Data getNodeData(FunctionScope functionScope, VariableScope variableScope,
                                                                   ParameterScope parameterScope) {
        var returnScope = new ReturnScopeImplementation();
        node.evaluate(functionScope, variableScope, parameterScope, returnScope);
        return returnScope.lookup();
    }

}