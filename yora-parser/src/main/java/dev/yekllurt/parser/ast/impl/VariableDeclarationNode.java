package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.ExecutionError;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariableDeclarationNode implements ASTNode {

    private final DataType type;
    private final String identifier;
    private final ASTNode value;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        var childReturnScope = new ReturnScopeImplementation();
        value.evaluate(functionScope, variableScope, parameterScope, childReturnScope);

        var data = childReturnScope.lookup();
        if (data.isNumber()) {
            if (data.isLong() && DataType.INT.equals(type)) {
                variableScope.assignVariable(identifier, type, data.data());
            } else if (data.isLong() && DataType.FLOAT.equals(type)) {
                variableScope.assignVariable(identifier, type, data.toDouble());
            } else if (data.isDouble() && DataType.INT.equals(type)) {
                throw new ExecutionError(String.format("Can't declare the variable '%s' of data type '%s' with data of the data type '%s'", identifier, data.dataType(), type));
            } else if (data.isDouble() && DataType.FLOAT.equals(type)) {
                variableScope.assignVariable(identifier, type, data.data());
            }
        } else if (data.isString()) {
            if (DataType.STRING.equals(type)) {
                variableScope.assignVariable(identifier, type, data.data());
            } else {
                throw new ExecutionError(String.format("Can't declare the variable '%s' of data type '%s' with data of the data type '%s'", identifier, type, data.dataType()));
            }
        } else if (data.isArray()) {
            if (data.isStringArray() && DataType.STRING_ARRAY.equals(type)) {
                variableScope.assignVariable(identifier, type, data.data());
            } else if (data.isLongArray() && DataType.INT_ARRAY.equals(type)) {
                variableScope.assignVariable(identifier, type, data.data());
            } else if (data.isDoubleArray() && DataType.FLOAT_ARRAY.equals(type)) {
                variableScope.assignVariable(identifier, type, data.data());
            } else {
                throw new ExecutionError(String.format("Can't declare the variable '%s' of data type '%s' with data of the data type '%s'", identifier, type, data.dataType()));
            }
        } else {
            throw new ExecutionError(String.format("Unable to declare a variable with the data type '%s'", data.dataType()));
        }
    }

}
