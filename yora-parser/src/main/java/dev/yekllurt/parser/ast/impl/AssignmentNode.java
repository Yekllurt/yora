package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.scope.*;
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
            performVariableAssignment(functionScope, variableScope, parameterScope, variableScope, childReturnScopeValue);
        } else if (parameterScope.existsData(identifier)) {
            performVariableAssignment(functionScope, variableScope, parameterScope, parameterScope, childReturnScopeValue);
        } else {
            throw new ExecutionError(String.format("Unable to resolve the variable '%s'", identifier));
        }
    }

    private void performVariableAssignment(FunctionScope functionScope, VariableScope variableScope,
                                           ParameterScope parameterScope,
                                           DataScope scopeToUpdate, ReturnScopeImplementation childReturnScope) {
        var variable = scopeToUpdate.lookup(identifier);
        // TODO: perform data type checks like in the variable declaration node
        if (variable.isArray()) {
            if (Objects.isNull(index)) {
                scopeToUpdate.updateData(identifier, childReturnScope.lookupReturnValue());
            } else {
                var returnScopeIndex = new ReturnScopeImplementation();
                index.evaluate(functionScope, variableScope, parameterScope, returnScopeIndex);
                updateArray(scopeToUpdate, returnScopeIndex.lookupReturnValue(), childReturnScope.lookupReturnValue());
            }
        } else {
            scopeToUpdate.updateData(identifier, childReturnScope.lookupReturnValue());
        }
    }

    private void updateArray(DataScope scope, Object index, Object updateValue) {
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
        } else if (DataType.BOOLEAN_ARRAY.equals(data.dataType())) {
            var temp = data.toBooleanArray();
            assertNotOutOfBounds(temp.length, indexInt);
            temp[indexInt] = (boolean) updateValue;
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
