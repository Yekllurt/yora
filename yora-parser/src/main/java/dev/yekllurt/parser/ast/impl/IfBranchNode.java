package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.collection.SequencedCollection;
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
public class IfBranchNode implements ASTNode {

    private final ConditionNode condition;
    private final SequencedCollection<ASTNode> statements;
    private final ASTNode returnStatement;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {

        // Condition evaluation
        var returnScopeCondition = new ReturnScopeImplementation();
        condition.evaluate(functionScope, variableScope, parameterScope, returnScopeCondition);
        if (!TokenType.KEYWORD_BOOLEAN.equals(returnScopeCondition.lookupReturnValueType())) {
            throw new ExecutionError(String.format("A condition returned the non-boolean value '%s'", returnScopeCondition.lookupReturnValueType()));
        }
        var conditionEvaluation = (boolean) returnScopeCondition.lookupReturnValue();

        if (conditionEvaluation) {
            variableScope.beginSoftScope();

            // Statement evaluation
            for (var statement : statements) {
                statement.evaluate(functionScope, variableScope, parameterScope, null);
            }

            // Return statement evaluation
            if (Objects.nonNull(returnStatement)) {
                var childReturnScope = new ReturnScopeImplementation();
                returnStatement.evaluate(functionScope, variableScope, parameterScope, childReturnScope);
                returnScope.assignReturnValue(childReturnScope.lookupReturnValueType(), childReturnScope.lookupReturnValue());
            }

            variableScope.endSoftScope();
        }
    }

}
