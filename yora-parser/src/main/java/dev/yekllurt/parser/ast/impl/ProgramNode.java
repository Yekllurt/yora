package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.collection.SequencedCollection;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ParameterScopeImplementation;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgramNode implements ASTNode {

    private final SequencedCollection<FunctionNode> functions;

    @Override
    public void evaluate(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        for (var function : functions) {
            var childParameterScope = new ParameterScopeImplementation();
            var childReturnScope = new ReturnScopeImplementation();
            function.evaluate(variableScope, childParameterScope, childReturnScope);
        }
        int x = 0;
    }

}
