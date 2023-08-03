package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.errors.ExecutionError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.utility.ParserUtility;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
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
            case TokenType.PUNCTUATION_PLUS -> performAdd(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_MINUS ->
                    performSubtract(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_STAR ->
                    performMultiply(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_DIVIDE ->
                    performDivide(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_CARET -> performPower(functionScope, variableScope, parameterScope, returnScope);
            case TokenType.PUNCTUATION_PERCENT ->
                    performModulo(functionScope, variableScope, parameterScope, returnScope);
            default ->
                    ExceptionUtility.throwException(ExecutionError.INVALID_ARITHMETIC_UNSUPPORTED_OPERATION, operator);
        }
    }

    private void performAdd(FunctionScope functionScope, VariableScope variableScope,
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

    private void performSubtract(FunctionScope functionScope, VariableScope variableScope,
                                 ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeData.x().dataType()) && ParserUtility.isNumber(nodeData.y().dataType())) {
            if (nodeData.x().isDouble() || nodeData.y().isDouble()) {
                returnScope.assignReturnValue(DataType.FLOAT, nodeData.x().toDouble() - nodeData.y().toDouble());
            } else {
                returnScope.assignReturnValue(DataType.INT, nodeData.x().toLong() - nodeData.y().toLong());
            }
        } else {
            ExceptionUtility.throwException(ExecutionError.INVALID_ARITHMETIC_OPERATION,
                    "subtract", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName(), "numbers");
        }
    }

    private void performMultiply(FunctionScope functionScope, VariableScope variableScope,
                                 ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeData.x().dataType()) && ParserUtility.isNumber(nodeData.y().dataType())) {
            if (nodeData.x().isDouble() || nodeData.y().isDouble()) {
                returnScope.assignReturnValue(DataType.FLOAT, nodeData.x().toDouble() * nodeData.y().toDouble());
            } else {
                returnScope.assignReturnValue(DataType.INT, nodeData.x().toLong() * nodeData.y().toLong());
            }
        } else {
            ExceptionUtility.throwException(ExecutionError.INVALID_ARITHMETIC_OPERATION,
                    "multiply", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName(), "numbers");
        }
    }

    private void performDivide(FunctionScope functionScope, VariableScope variableScope,
                               ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        if (ParserUtility.isNumber(nodeData.x().dataType()) && ParserUtility.isNumber(nodeData.y().dataType())) {
            ExceptionUtility.throwExceptionIf(nodeData.y().toDouble() == 0,
                    ExecutionError.INVALID_ARITHMETIC_CANT_DIVIDE_BY_ZERO);
            if (nodeData.x().isDouble() || nodeData.y().isDouble()) {
                returnScope.assignReturnValue(DataType.FLOAT, nodeData.x().toDouble() / nodeData.y().toDouble());
            } else {
                returnScope.assignReturnValue(DataType.INT, nodeData.x().toLong() / nodeData.y().toLong());
            }
        } else {
            ExceptionUtility.throwException(ExecutionError.INVALID_ARITHMETIC_OPERATION,
                    "divide", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName(), "numbers");
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
            ExceptionUtility.throwException(ExecutionError.INVALID_ARITHMETIC_OPERATION,
                    "power", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName(), "numbers");
        }
    }

    private void performModulo(FunctionScope functionScope, VariableScope variableScope,
                               ParameterScope parameterScope, ReturnScope returnScope) {
        var nodeData = getNodeData(functionScope, variableScope, parameterScope);
        ExceptionUtility.throwExceptionIf(nodeData.y().toDouble() == 0,
                ExecutionError.INVALID_ARITHMETIC_CANT_DIVIDE_BY_ZERO);
        if (nodeData.x().isLong() && nodeData.y().isLong()) {
            returnScope.assignReturnValue(DataType.INT, nodeData.x().toLong() % nodeData.y().toLong());
        } else {
            ExceptionUtility.throwException(ExecutionError.INVALID_ARITHMETIC_OPERATION,
                    "modulo", nodeData.x().getClass().getSimpleName(), nodeData.y().getClass().getSimpleName(), "integers");
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
