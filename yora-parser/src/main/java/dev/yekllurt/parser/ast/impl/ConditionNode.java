package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.ConditionOperator;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;
import dev.yekllurt.parser.token.TokenType;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class ConditionNode implements ASTNode {

    private final ASTNode left;
    private final ASTNode right;
    private final ConditionOperator operator;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        var returnScopeLeft = new ReturnScopeImplementation();
        var returnScopeRight = new ReturnScopeImplementation();
        left.evaluate(functionScope, variableScope, null, returnScopeLeft);
        right.evaluate(functionScope, variableScope, null, returnScopeRight);
        if (Objects.isNull(returnScopeLeft.lookupReturnValue()) && Objects.isNull(returnScopeRight.lookupReturnValue())) {
            throw new ExecutionError("Couldn't compare two values as they are both null and null values are not supported by the language");
        }
        switch (operator) {
            case EQUAL -> {
                var equal = Objects.isNull(returnScopeRight.lookupReturnValue())
                        ? returnScopeLeft.lookupReturnValue().equals(returnScopeRight.lookupReturnValue())
                        : returnScopeRight.lookupReturnValue().equals(returnScopeLeft.lookupReturnValue());
                returnScope.assignReturnValue(TokenType.KEYWORD_BOOLEAN, equal);
            }
            case NOT_EQUAL -> {
                var equal = Objects.isNull(returnScopeRight.lookupReturnValue())
                        ? returnScopeLeft.lookupReturnValue().equals(returnScopeRight.lookupReturnValue())
                        : returnScopeRight.lookupReturnValue().equals(returnScopeLeft.lookupReturnValue());
                returnScope.assignReturnValue(TokenType.KEYWORD_BOOLEAN, !equal);
            }
            default ->
                    throw new UnsupportedOperationException("The condition operator '%s' is not supported".formatted(operator));
        }
    }

}
