package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.api.collection.SequencedCollection;
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

        variableScope.beginSoftScope();

        // Statement evaluation
        for (var statement : statements) {
            if (statement instanceof IfBranchNode ifBranchNode) {
                var childReturnScope = new ReturnScopeImplementation();
                ifBranchNode.evaluate(functionScope, variableScope, parameterScope, childReturnScope);
                // This is the case where someone uses a return statement in an if control flow
                if (Objects.nonNull(childReturnScope.lookupReturnValueType())) {
                    returnScope.assignReturnValue(childReturnScope.lookupReturnValueType(), childReturnScope.lookupReturnValue());
                    variableScope.endSoftScope();
                    return;
                }
            } else {
                statement.evaluate(functionScope, variableScope, parameterScope, null);
            }
        }

        // Return statement evaluation
        if (Objects.nonNull(returnStatement)) {
            var childReturnScope = new ReturnScopeImplementation();
            returnStatement.evaluate(functionScope, variableScope, parameterScope, childReturnScope);
            returnScope.assignReturnValue(childReturnScope.lookupReturnValueType(), childReturnScope.lookupReturnValue());
        }

        variableScope.endSoftScope();

    }

}
