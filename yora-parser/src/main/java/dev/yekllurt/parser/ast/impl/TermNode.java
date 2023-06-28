package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;
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
        if (ParserUtility.isIdentifier(value)) {
            String identifier = (String) value;
            if (variableScope.existsVariable(identifier)) {
                returnScope.assignReturnValue(variableScope.lookupVariableType(identifier), variableScope.lookupVariable(identifier));
            } else if (parameterScope.existsParameter(identifier)) {
                returnScope.assignReturnValue(parameterScope.lookupParameterType(identifier), parameterScope.lookupParameter(identifier));
            } else {
                throw new ExecutionError(String.format("Unable to resolve the variable '%s'", identifier));
            }
        } else if (ParserUtility.isLong(value)) {
            returnScope.assignReturnValue(DataType.INT, Integer.valueOf((String) value));
        } else if (ParserUtility.isDouble(value)) {
            returnScope.assignReturnValue(DataType.FLOAT, Integer.valueOf((String) value));
        } else if (ParserUtility.isString(value)) {
            returnScope.assignReturnValue(DataType.STRING, String.valueOf(value));
        } else {
            throw new ExecutionError(String.format("Unable to resolve the term '%s'", value));
        }
    }

}
