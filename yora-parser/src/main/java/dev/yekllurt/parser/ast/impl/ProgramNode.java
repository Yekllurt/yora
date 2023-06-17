package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.collection.SequencedCollection;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.FunctionScopeImplementation;
import dev.yekllurt.parser.interpreter.scope.impl.ParameterScopeImplementation;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
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
