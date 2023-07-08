package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.throwable.ExecutionError;
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
                returnScope.assignReturnValue(variableScope.lookupVariableType(identifier), variableScope.lookupVariable(identifier));
            } else if (parameterScope.existsParameter(identifier)) {
                returnScope.assignReturnValue(parameterScope.lookupParameterType(identifier), parameterScope.lookupParameter(identifier));
            } else {
                throw new ExecutionError(String.format("Unable to resolve the variable '%s'", identifier));
            }
        } else {
            var dataType = ParserUtility.getReturnType(value);
            switch (dataType) {
                case INT -> returnScope.assignReturnValue(DataType.INT, Long.valueOf((String) value));
                case FLOAT -> returnScope.assignReturnValue(DataType.FLOAT, Double.valueOf((String) value));
                case STRING -> returnScope.assignReturnValue(DataType.STRING, String.valueOf(value));
                case INT_ARRAY -> returnScope.assignReturnValue(DataType.INT_ARRAY, value);
                case FLOAT_ARRAY -> returnScope.assignReturnValue(DataType.FLOAT_ARRAY, value);
                case STRING_ARRAY -> returnScope.assignReturnValue(DataType.STRING_ARRAY, value);
                default -> throw new ExecutionError(String.format("Unable to resolve the term '%s'", value));
            }
        }
    }

}
