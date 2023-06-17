package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.Utility;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;
import dev.yekllurt.parser.token.TokenType;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class TermNode implements ASTNode {

    public enum TermType {
        LITERAL,
        DYNAMIC;
    }

    private final Object value;
    private final TermType type;

    @Override
    public void evaluate(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        if (Objects.isNull(returnScope)) {
            throw new ExecutionError("Unable to return a value as the return scope is null");
        }
        if (Utility.isIdentifier(value)) {
            String identifier = (String) value;
            returnScope.assignReturnValue(variableScope.lookupVariableType(identifier), variableScope.lookupVariable(identifier));
        } else if (Utility.isInteger(value)) {
            returnScope.assignReturnValue(TokenType.KEYWORD_INT, Integer.valueOf((String) value));
        } else if (Utility.isFloat(value)) {
            returnScope.assignReturnValue(TokenType.KEYWORD_FLOAT, Integer.valueOf((String) value));
        } else {
            throw new ExecutionError(String.format("Unable to resolve the term '%s'", value));
        }
    }

}
