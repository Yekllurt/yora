package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.ExecutionError;
import dev.yekllurt.parser.interpreter.throwable.InvalidOperationError;
import dev.yekllurt.parser.utility.ParserUtility;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class AssignmentNode implements ASTNode {

    private final String identifier;
    private ASTNode index;              // Only used if the variable type is an array
    private final ASTNode value;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        var childReturnScopeValue = new ReturnScopeImplementation();
        value.evaluate(functionScope, variableScope, parameterScope, childReturnScopeValue);

        if (variableScope.existsData(identifier)) {
            performVariableScopeAssignment(functionScope, variableScope, parameterScope, returnScope, childReturnScopeValue);
        } else if (parameterScope.existsData(identifier)) {
            performParameterScopeAssignment(functionScope, variableScope, parameterScope, returnScope, childReturnScopeValue);
        } else {
            throw new ExecutionError(String.format("Unable to resolve the variable '%s'", identifier));
        }
    }

    private void performVariableScopeAssignment(FunctionScope functionScope, VariableScope variableScope,
                                                ParameterScope parameterScope, ReturnScope returnScope,
                                                ReturnScopeImplementation childReturnScope) {
        var variable = variableScope.lookup(identifier);
        if (variable.isArray()) {
            assertIndexNotNull();
            var returnScopeIndex = new ReturnScopeImplementation();
            index.evaluate(functionScope, variableScope, parameterScope, returnScopeIndex);
            updateArray(variableScope, returnScopeIndex.lookupReturnValue(), childReturnScope.lookupReturnValue());
        } else {
            variableScope.updateData(identifier, childReturnScope.lookupReturnValue());
        }
    }

    private void performParameterScopeAssignment(FunctionScope functionScope, VariableScope variableScope,
                                                 ParameterScope parameterScope, ReturnScope returnScope,
                                                 ReturnScopeImplementation childReturnScope) {
        var variable = parameterScope.lookup(identifier);
        if (variable.isArray()) {
            var returnScopeIndex = new ReturnScopeImplementation();
            index.evaluate(functionScope, variableScope, parameterScope, returnScopeIndex);
            updateArray(parameterScope, returnScopeIndex.lookupReturnValue(), childReturnScope.lookupReturnValue());
        } else {
            parameterScope.updateData(identifier, childReturnScope.lookupReturnValue());
        }
    }

    private void updateArray(VariableScope scope, Object index, Object updateValue) {
        var data = scope.lookup(identifier);
        var indexInt = parseIndex(index);

        if (DataType.STRING_ARRAY.equals(data.dataType())) {
            var temp = data.toStringArray();
            assertNotOutOfBounds(temp.length, indexInt);
            temp[indexInt] = (String) updateValue;
            scope.updateData(identifier, temp);
        } else if (DataType.INT_ARRAY.equals(data.dataType())) {
            var temp = data.toLongArray();
            assertNotOutOfBounds(temp.length, indexInt);
            temp[indexInt] = (Long) updateValue;
            scope.updateData(identifier, temp);
        } else if (DataType.FLOAT_ARRAY.equals(data.dataType())) {
            var temp = data.toDoubleArray();
            assertNotOutOfBounds(temp.length, indexInt);
            temp[indexInt] = (Double) updateValue;
            scope.updateData(identifier, temp);
        } else {
            throw new InvalidOperationError(String.format("Failed updating the array of data type '%s' as it is not supported", data.dataType()));
        }
    }

    private void updateArray(ParameterScope scope, Object index, Object updateValue) {
        var data = scope.lookup(identifier);
        var indexInt = parseIndex(index);

        if (DataType.STRING_ARRAY.equals(data.dataType())) {
            var temp = data.toStringArray();
            assertNotOutOfBounds(temp.length, indexInt);
            temp[indexInt] = (String) updateValue;
            scope.updateData(identifier, temp);
        } else if (DataType.INT_ARRAY.equals(data.dataType())) {
            var temp = data.toLongArray();
            assertNotOutOfBounds(temp.length, indexInt);
            temp[indexInt] = (Long) updateValue;
            scope.updateData(identifier, temp);
        } else if (DataType.FLOAT_ARRAY.equals(data.dataType())) {
            var temp = data.toDoubleArray();
            assertNotOutOfBounds(temp.length, indexInt);
            temp[indexInt] = (Double) updateValue;
            scope.updateData(identifier, temp);
        } else {
            throw new InvalidOperationError(String.format("Failed updating the array of data type '%s' as it is not supported", data.dataType()));
        }
    }

    private void assertIndexNotNull() {
        if (Objects.isNull(index)) {
            throw new InvalidOperationError(String.format("Failed updating an index value for the variable '%s' as the index is not defined", identifier));
        }
    }

    private int parseIndex(Object index) {
        if (!ParserUtility.isLong(index)) {
            throw new InvalidOperationError(String.format("Failed parsing array index for the index %s", index));
        }
        if (ParserUtility.parseLong(index) != ParserUtility.parseLong(index).intValue()) {
            throw new InvalidOperationError(String.format("Failed parsing array index %s as when using converting it from an int64 to int32 there is an information loss", index));
        }
        return ParserUtility.parseLong(index).intValue();
    }

    private void assertNotOutOfBounds(int arrayLength, int index) {
        if (index >= arrayLength) {
            throw new IndexOutOfBoundsException("Index %s is out of bounds for length %s".formatted(arrayLength, index));
        }
    }

}
