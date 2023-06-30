package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.utility.ParserUtility;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.InvalidOperationError;
import dev.yekllurt.parser.token.TokenType;
import dev.yekllurt.api.tuples.Tuple;
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
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeData.x().dataType()) && ParserUtility.isNumber(nodeData.y().dataType())) {
            if (nodeData.x().isDouble() || nodeData.y().isDouble()) {
                returnScope.assignReturnValue(DataType.FLOAT, nodeData.x().toDouble() + nodeData.y().toDouble());
            } else {
                returnScope.assignReturnValue(DataType.INT, nodeData.x().toLong() + nodeData.y().toLong());
            }
        } else {
            returnScope.assignReturnValue(DataType.STRING, nodeData.x().toString() + nodeData.y().toString());
        }
    }

    private void performMinus(FunctionScope functionScope, VariableScope variableScope,
                              ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeData.x().dataType()) && ParserUtility.isNumber(nodeData.y().dataType())) {
            if (nodeData.x().isDouble() || nodeData.y().isDouble()) {
                returnScope.assignReturnValue(DataType.FLOAT, nodeData.x().toDouble() - nodeData.y().toDouble());
            } else {
                returnScope.assignReturnValue(DataType.INT, nodeData.x().toLong() - nodeData.y().toLong());
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to subtract the values '%s' and '%s' with each other, both must be numbers", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName()));
        }
    }

    private void performStar(FunctionScope functionScope, VariableScope variableScope,
                             ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeData.x().dataType()) && ParserUtility.isNumber(nodeData.y().dataType())) {
            if (nodeData.x().isDouble() || nodeData.y().isDouble()) {
                returnScope.assignReturnValue(DataType.FLOAT, nodeData.x().toDouble() * nodeData.y().toDouble());
            } else {
                returnScope.assignReturnValue(DataType.INT, nodeData.x().toLong() * nodeData.y().toLong());
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to multiply the values '%s' and '%s' with each other, both must be numbers", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName()));
        }
    }

    private void performDivide(FunctionScope functionScope, VariableScope variableScope,
                               ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeData.x().dataType()) && ParserUtility.isNumber(nodeData.y().dataType())) {
            if (nodeData.y().toDouble() == 0) {
                throw new InvalidOperationError("Can't divide by 0");
            }
            if (nodeData.x().isDouble() || nodeData.y().isDouble()) {
                returnScope.assignReturnValue(DataType.FLOAT, nodeData.x().toDouble() / nodeData.y().toDouble());
            } else {
                returnScope.assignReturnValue(DataType.INT, nodeData.x().toLong() / nodeData.y().toLong());
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to divide the values '%s' and '%s' with each other, both must be numbers", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName()));
        }
    }

    private void performPower(FunctionScope functionScope, VariableScope variableScope,
                              ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeData.x().dataType()) && ParserUtility.isNumber(nodeData.y().dataType())) {
            if (nodeData.x().isDouble() || nodeData.y().isDouble()) {
                returnScope.assignReturnValue(DataType.FLOAT, Math.pow(nodeData.x().toDouble(), nodeData.y().toDouble()));
            } else {
                returnScope.assignReturnValue(DataType.INT, (long) Math.pow(nodeData.x().toLong(), nodeData.y().toLong()));
            }
        } else {
            throw new InvalidOperationError(String.format("Unable to power the values '%s' and '%s' with each other, both must be numbers", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName()));
        }
    }

    private void performPercent(FunctionScope functionScope, VariableScope variableScope,
                                ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (nodeData.x().isLong() && nodeData.y().isLong()) {
            returnScope.assignReturnValue(DataType.INT, nodeData.x().toLong() % nodeData.y().toLong());
        } else {
            throw new InvalidOperationError(String.format("Unable to modulo the values '%s' and '%s' with each other, both must be integers", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName()));
        }
    }

    private Tuple<dev.yekllurt.parser.interpreter.scope.Data, dev.yekllurt.parser.interpreter.scope.Data> getNodeData(FunctionScope functionScope, VariableScope variableScope,
                                                                                                                      ParameterScope parameterScope) {
        var returnScopeLeft = new ReturnScopeImplementation();
        left.evaluate(functionScope, variableScope, parameterScope, returnScopeLeft);
        var returnScopeRight = new ReturnScopeImplementation();
        right.evaluate(functionScope, variableScope, parameterScope, returnScopeRight);
        return new Tuple<>(returnScopeLeft.lookup(), returnScopeRight.lookup());
    }

}
