package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.errors.ExecutionError;
import dev.yekllurt.api.errors.ScopeError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.interpreter.interpreter.nativ.variable.NativeVariableDirectory;
import dev.yekllurt.interpreter.interpreter.scope.*;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
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
        ExceptionUtility.throwExceptionIf(Objects.isNull(returnScope),
                ScopeError.RETURN_SCOPE_IS_NULL);

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
                ExceptionUtility.throwException(ExecutionError.INVALID_VARIABLE_EXISTS_IN_NO_SCOPE,
                        identifier, "[native, variable, parameter]");
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
                default -> ExceptionUtility.throwException(ExecutionError.INVALID_TYPE_UNSUPPORTED_TERM, value);
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
