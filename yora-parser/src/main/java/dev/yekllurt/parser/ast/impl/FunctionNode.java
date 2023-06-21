package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.collection.SequencedCollection;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class FunctionNode implements ASTNode {

    private final String identifier;
    private final String returnType;

    private final SequencedCollection<ParameterNode> parameters;
    private final SequencedCollection<ASTNode> statements;
    private final ASTNode returnStatement;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        // TODO: handle parameters, amongst others these have to be evaluated

        variableScope.beginScope();

        for (var statement : statements) {
            statement.evaluate(functionScope, variableScope, parameterScope, null);
        }

        if (Objects.nonNull(returnStatement)) {
            var childReturnScope = new ReturnScopeImplementation();
            returnStatement.evaluate(functionScope, variableScope, parameterScope, childReturnScope);
            returnScope.assignReturnValue(returnType, childReturnScope.lookupReturnValue());
        }

        variableScope.endScope();

    }

}
