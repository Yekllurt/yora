package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.api.collection.SequencedCollection;
import dev.yekllurt.interpreter.interpreter.scope.FunctionScope;
import dev.yekllurt.interpreter.interpreter.scope.ParameterScope;
import dev.yekllurt.interpreter.interpreter.scope.ReturnScope;
import dev.yekllurt.interpreter.interpreter.scope.VariableScope;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.interpreter.interpreter.throwable.ExecutionError;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WhileBranchNode implements ASTNode {

    private final ConditionNode condition;
    private final SequencedCollection<ASTNode> statements;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        boolean run = evaluateCondition(functionScope, variableScope, parameterScope);
        while (run) {
            variableScope.beginSoftScope();
            for (var statement : statements) {
                statement.evaluate(functionScope, variableScope, parameterScope, null);
            }
            variableScope.endSoftScope();
            run = evaluateCondition(functionScope, variableScope, parameterScope);
        }

    }

    private boolean evaluateCondition(FunctionScope functionScope, VariableScope variableScope, ParameterScope parameterScope) {
        var returnScopeCondition = new ReturnScopeImplementation();
        condition.evaluate(functionScope, variableScope, parameterScope, returnScopeCondition);
        if (!DataType.BOOLEAN.equals(returnScopeCondition.lookupReturnValueType())) {
            throw new ExecutionError(String.format("A condition returned the non-boolean value '%s'",
                    returnScopeCondition.lookupReturnValueType()));
        }
        return returnScopeCondition.lookup().toBoolean();
    }

}
