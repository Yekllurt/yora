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
        // TODO: only execute a function if it is the main function, if multiple main functions exist, then throw an error
        for (var function : functions) {
            if (function.getIdentifier().equals("main")) {
                var childParameterScope = new ParameterScopeImplementation();
                var childReturnScope = new ReturnScopeImplementation();
                function.evaluate(variableScope, childParameterScope, childReturnScope);
            }
        }
    }

}
