package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.interpreter.interpreter.nativ.variable.NativeVariableDirectory;
import dev.yekllurt.interpreter.interpreter.scope.*;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.interpreter.interpreter.throwable.ExecutionError;
import dev.yekllurt.interpreter.interpreter.throwable.InvalidOperationError;
import dev.yekllurt.interpreter.utility.ParserUtility;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class TermNode implements ASTNode {

    public enum TermType {
        LITERAL,
        DYNAMIC
    }

    private final Object value;
    private ASTNode index;              // Only used if the variable type is an array
    private final TermType type;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        if (Objects.isNull(returnScope)) {
            throw new ExecutionError("Unable to return a value as the return scope is null");
        }

        // This must be before the STRING check as an identifier is also a STRING
        if (TermType.DYNAMIC.equals(type) && ParserUtility.isIdentifier(value)) {
            String identifier = (String) value;
            if (NativeVariableDirectory.isNativeVariable(identifier)) {
                var nativeVariable = NativeVariableDirectory.getNativeVariable(identifier);
                var nativeVariableValue = nativeVariable.getValue();
                var nativeVariableDataType = ParserUtility.getReturnType(nativeVariableValue);
                returnScope.assignReturnValue(nativeVariableDataType, nativeVariableValue);
            } else if (variableScope.existsData(identifier)) {
                performLookup(functionScope, variableScope, parameterScope, returnScope, variableScope, identifier);
            } else if (parameterScope.existsData(identifier)) {
                performLookup(functionScope, variableScope, parameterScope, returnScope, parameterScope, identifier);
            } else {
                throw new ExecutionError(String.format("Unable to resolve the variable '%s'", identifier));
            }
        } else {
            var dataType = ParserUtility.getReturnType(value);
            switch (dataType) {
                case INT -> returnScope.assignReturnValue(DataType.INT, ParserUtility.parseLong(value));
                case FLOAT -> returnScope.assignReturnValue(DataType.FLOAT, ParserUtility.parseDouble((String) value));
                case STRING -> returnScope.assignReturnValue(DataType.STRING, String.valueOf(value));
                case BOOLEAN -> returnScope.assignReturnValue(DataType.BOOLEAN, value);
                case INT_ARRAY -> returnScope.assignReturnValue(DataType.INT_ARRAY, value);
                case FLOAT_ARRAY -> returnScope.assignReturnValue(DataType.FLOAT_ARRAY, value);
                case STRING_ARRAY -> returnScope.assignReturnValue(DataType.STRING_ARRAY, value);
                case BOOLEAN_ARRAY -> returnScope.assignReturnValue(DataType.BOOLEAN_ARRAY, value);
                default -> throw new ExecutionError(String.format("Unable to resolve the term '%s'", value));
            }
        }
    }

    private void performLookup(FunctionScope functionScope, VariableScope variableScope,
                               ParameterScope parameterScope, ReturnScope returnScope,
                               DataScope lookupScope, String identifier) {
        if (lookupScope.lookup(identifier).isArray()) {
            if (Objects.isNull(index)) {
                returnScope.assignReturnValue(lookupScope.lookupDataType(identifier), lookupScope.lookupData(identifier));
            } else {
                var returnScopeIndex = new ReturnScopeImplementation();
                index.evaluate(functionScope, variableScope, parameterScope, returnScopeIndex);
                updateReturnScope(returnScopeIndex.lookupReturnValue(), lookupScope.lookup(identifier), returnScope);
            }
        } else {
            returnScope.assignReturnValue(lookupScope.lookupDataType(identifier), lookupScope.lookupData(identifier));
        }
    }

    private void updateReturnScope(Object index, dev.yekllurt.interpreter.interpreter.scope.Data data, ReturnScope returnScope) {
        var indexInt = parseIndex(index);

        if (DataType.STRING_ARRAY.equals(data.dataType())) {
            var temp = data.toStringArray();
            assertNotOutOfBounds(temp.length, indexInt);
            returnScope.assignReturnValue(DataType.STRING, temp[indexInt]);
        } else if (DataType.INT_ARRAY.equals(data.dataType())) {
            var temp = data.toLongArray();
            assertNotOutOfBounds(temp.length, indexInt);
            returnScope.assignReturnValue(DataType.INT, temp[indexInt]);
        } else if (DataType.FLOAT_ARRAY.equals(data.dataType())) {
            var temp = data.toDoubleArray();
            assertNotOutOfBounds(temp.length, indexInt);
            returnScope.assignReturnValue(DataType.FLOAT, temp[indexInt]);
        } else if (DataType.BOOLEAN_ARRAY.equals(data.dataType())) {
            var temp = data.toBooleanArray();
            assertNotOutOfBounds(temp.length, indexInt);
            returnScope.assignReturnValue(DataType.BOOLEAN, temp[indexInt]);
        } else {
            throw new InvalidOperationError(String.format("Failed updating the array of data type '%s' as it is not supported", data.dataType()));
        }
    }

    private int parseIndex(Object index) {
        if (!ParserUtility.isLong(index)) {
            throw new InvalidOperationError(String.format("Failed parsing array index for the index '%s'", index));
        }
        if (ParserUtility.parseLong(index) != ParserUtility.parseLong(index).intValue()) {
            throw new InvalidOperationError(String.format("Failed parsing array index '%s' as when using converting it from an int64 to int32 there is an information loss", index));
        }
        return ParserUtility.parseLong(index).intValue();
    }

    private void assertNotOutOfBounds(int arrayLength, int index) {
        if (index >= arrayLength) {
            throw new IndexOutOfBoundsException("Index %s is out of bounds for length %s".formatted(arrayLength, index));
        }
    }

}
