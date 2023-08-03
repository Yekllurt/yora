package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.api.collection.SequencedCollection;
import dev.yekllurt.interpreter.interpreter.scope.FunctionScope;
import dev.yekllurt.interpreter.interpreter.scope.ParameterScope;
import dev.yekllurt.interpreter.interpreter.scope.ReturnScope;
import dev.yekllurt.interpreter.interpreter.scope.VariableScope;
import dev.yekllurt.interpreter.interpreter.scope.impl.ParameterScopeImplementation;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgramNode implements ASTNode {

    private final SequencedCollection<FunctionNode> functions;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        functions.forEach(function -> functionScope.assignFunction(function.getIdentifier(), function));

        var mainFunction = functionScope.lookupFunction("main");
        var childParameterScope = new ParameterScopeImplementation();
        var childReturnScope = new ReturnScopeImplementation();
        mainFunction.evaluate(functionScope, variableScope, childParameterScope, childReturnScope);
    }

}
