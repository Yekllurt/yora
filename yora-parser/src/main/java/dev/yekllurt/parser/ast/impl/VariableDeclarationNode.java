package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariableDeclarationNode implements ASTNode {

    private final String type;
    private final String identifier;
    private final ASTNode value;

    @Override
    public void evaluate(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        var childReturnScope = new ReturnScopeImplementation();
        value.evaluate(variableScope, null, childReturnScope);

        variableScope.assignVariable(identifier, type, childReturnScope.lookupReturnValue());
    }

}
