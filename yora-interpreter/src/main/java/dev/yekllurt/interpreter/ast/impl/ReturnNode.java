package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.interpreter.interpreter.scope.FunctionScope;
import dev.yekllurt.interpreter.interpreter.scope.ParameterScope;
import dev.yekllurt.interpreter.interpreter.scope.ReturnScope;
import dev.yekllurt.interpreter.interpreter.scope.VariableScope;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReturnNode implements ASTNode {

    private final ASTNode value;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        var childReturnScope = new ReturnScopeImplementation();
        value.evaluate(functionScope, variableScope, parameterScope, childReturnScope);

        returnScope.assignReturnValue(childReturnScope.lookupReturnValueType(), childReturnScope.lookupReturnValue());
    }

}
