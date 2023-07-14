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
        if (ParserUtility.isIdentifier(value)) {
            String identifier = (String) value;
            if (variableScope.existsVariable(identifier)) {
                if (ParserUtility.isArray(variableScope.lookupVariableType(identifier))) {
                    if (Objects.isNull(index)) {
                        returnScope.assignReturnValue(variableScope.lookupVariableType(identifier), variableScope.lookupVariable(identifier));
                    } else {
                        var returnScopeIndex = new ReturnScopeImplementation();
                        index.evaluate(functionScope, variableScope, parameterScope, returnScopeIndex);
                        updateReturnScope(returnScopeIndex.lookupReturnValue(), variableScope.lookup(identifier), returnScope);
                    }
                } else {
                    returnScope.assignReturnValue(variableScope.lookupVariableType(identifier), variableScope.lookupVariable(identifier));
                }
            } else if (parameterScope.existsParameter(identifier)) {
                if (ParserUtility.isArray(parameterScope.lookupParameterType(identifier))) {
                    if (Objects.isNull(index)) {
                        returnScope.assignReturnValue(parameterScope.lookupParameterType(identifier), parameterScope.lookupParameter(identifier));

                    } else {
                        var returnScopeIndex = new ReturnScopeImplementation();
                        index.evaluate(functionScope, variableScope, parameterScope, returnScopeIndex);
                        updateReturnScope(returnScopeIndex.lookupReturnValue(), parameterScope.lookup(identifier), returnScope);
                    }
                } else {
                    returnScope.assignReturnValue(parameterScope.lookupParameterType(identifier), parameterScope.lookupParameter(identifier));
                }
            } else {
                throw new ExecutionError(String.format("Unable to resolve the variable '%s'", identifier));
            }
        } else {
            var dataType = ParserUtility.getReturnType(value);
            switch (dataType) {
                case INT -> returnScope.assignReturnValue(DataType.INT, ParserUtility.parseLong(value));
                case FLOAT -> returnScope.assignReturnValue(DataType.FLOAT, ParserUtility.parseDouble((String) value));
                case STRING -> returnScope.assignReturnValue(DataType.STRING, String.valueOf(value));
                case INT_ARRAY -> returnScope.assignReturnValue(DataType.INT_ARRAY, value);
                case FLOAT_ARRAY -> returnScope.assignReturnValue(DataType.FLOAT_ARRAY, value);
                case STRING_ARRAY -> returnScope.assignReturnValue(DataType.STRING_ARRAY, value);
                default -> throw new ExecutionError(String.format("Unable to resolve the term '%s'", value));
            }
        }
    }

    private void updateReturnScope(Object index, dev.yekllurt.parser.interpreter.scope.Data data, ReturnScope returnScope) {
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
