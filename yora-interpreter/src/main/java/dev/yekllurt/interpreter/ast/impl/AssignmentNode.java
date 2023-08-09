package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.errors.ExecutionError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.interpreter.interpreter.scope.*;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.interpreter.utility.ParserUtility;
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
            ExceptionUtility.throwException(ExecutionError.INVALID_VARIABLE_EXISTS_IN_NO_SCOPE,
                    identifier, "[variable, parameter]");
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
            ExceptionUtility.throwException(ExecutionError.INVALID_VARIABLE_UPDATE_INVALID_ARRAY_DATA_TYPE,
                    data.dataType());
        }
    }

    private int parseIndex(Object index) {
        ExceptionUtility.throwExceptionIf(!ParserUtility.isLong(index),
                ExecutionError.PARSE_EXCEPTION_INVALID_DATA_TYPE_FOR_ARRAY_INDEX,
                index);
        ExceptionUtility.throwExceptionIf(ParserUtility.parseLong(index) != ParserUtility.parseLong(index).intValue(),
                ExecutionError.PARSE_EXCEPTION_INVALID_DATA_TYPE_FOR_ARRAY_INDEX_AS_NO_INT_32,
                index);
        return ParserUtility.parseLong(index).intValue();
    }

    private void assertNotOutOfBounds(int arrayLength, int index) {
        ExceptionUtility.throwExceptionIf(index >= arrayLength,
                ExecutionError.INVALID_VARIABLE_ACCESS_ARRAY_INDEX_OUT_OF_BOUNDS,
                arrayLength, index);
    }

}
