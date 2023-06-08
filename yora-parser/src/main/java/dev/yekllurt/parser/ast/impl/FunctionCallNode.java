package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.interpreter.nativefunction.NativeFunctionDirectory;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class FunctionCallNode implements ASTNode {

    private final String functionIdentifier;
    private final ASTNode arguments;

    @Override
    public void evaluate(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope) {
        if (NativeFunctionDirectory.isNativeFunction(functionIdentifier)) {
            var function = NativeFunctionDirectory.getNativeFunction(functionIdentifier);
            if (arguments instanceof ExpressionListNode expressionList) {
                var arguments = new ArrayList<>();
                for (var expression : expressionList.getExpressionList()) {
                    var returnScopeExpression = new ReturnScopeImplementation();
                    expression.evaluate(variableScope, null, returnScopeExpression);
                    arguments.add(returnScopeExpression.lookupReturnValue());
                }
                function.execute(arguments.toArray());
            } else {
                throw new ExecutionError(String.format("Unknown node type %s for native function call", arguments.getClass().getSimpleName()));
            }
        } else {

        }
    }

}
