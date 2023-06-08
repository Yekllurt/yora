package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReturnNode implements ASTNode {

    private final ASTNode value;

    @Override
    public void evaluate(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        var childReturnScope = new ReturnScopeImplementation();
        value.evaluate(variableScope, null, childReturnScope);

        returnScope.assignReturnValue(null, childReturnScope.lookupReturnValue());
    }

}
