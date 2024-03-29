package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.api.errors.ExecutionError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.interpreter.interpreter.scope.FunctionScope;
import dev.yekllurt.interpreter.interpreter.scope.ParameterScope;
import dev.yekllurt.interpreter.interpreter.scope.ReturnScope;
import dev.yekllurt.interpreter.interpreter.scope.VariableScope;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.interpreter.token.TokenType;
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
            default ->
                    ExceptionUtility.throwException(ExecutionError.INVALID_ARITHMETIC_UNSUPPORTED_UNARY_OPERATION, operator);
        }
    }

    private void performPlus(FunctionScope functionScope, VariableScope variableScope,
                             ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (nodeData.isNumber()) {
            returnScope.assignReturnValue(nodeData.dataType(), nodeData.data());
        }
        ExceptionUtility.throwException(ExecutionError.INVALID_ARITHMETIC_UNSUPPORTED_UNARY_OPERATION_ON_DATATYPE,
                TokenType.PUNCTUATION_PLUS, nodeData.dataType());
    }

    private void performMinus(FunctionScope functionScope, VariableScope variableScope,
                              ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (nodeData.isNumber()) {
            if (nodeData.isDouble()) {
                returnScope.assignReturnValue(nodeData.dataType(), -nodeData.toDouble());
            } else {
                returnScope.assignReturnValue(nodeData.dataType(), -nodeData.toLong());
            }
            return;
        }
        ExceptionUtility.throwException(ExecutionError.INVALID_ARITHMETIC_UNSUPPORTED_UNARY_OPERATION_ON_DATATYPE,
                TokenType.PUNCTUATION_MINUS, nodeData.dataType());
    }

    private dev.yekllurt.interpreter.interpreter.scope.Data getNodeData(FunctionScope functionScope, VariableScope variableScope,
                                                                        ParameterScope parameterScope) {
        var returnScope = new ReturnScopeImplementation();
        node.evaluate(functionScope, variableScope, parameterScope, returnScope);
        return returnScope.lookup();
    }

}