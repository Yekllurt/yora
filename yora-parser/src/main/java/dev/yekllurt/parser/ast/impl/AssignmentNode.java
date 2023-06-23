package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignmentNode implements ASTNode {

    private final String identifier;
    private final ASTNode value;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        var childReturnScope = new ReturnScopeImplementation();
        value.evaluate(functionScope, variableScope, parameterScope, childReturnScope);

        if (variableScope.existsVariable(identifier)) {
            variableScope.updateVariable(identifier, childReturnScope.lookupReturnValue());
        } else if (parameterScope.existsParameter(identifier)) {
            parameterScope.updateParameter(identifier, childReturnScope.lookupReturnValue());
        } else {
            throw new ExecutionError(String.format("Unable to resolve the variable '%s'", identifier));
        }
    }

}
