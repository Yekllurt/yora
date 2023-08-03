package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.interpreter.interpreter.nativ.function.NativeFunctionDirectory;
import dev.yekllurt.interpreter.interpreter.scope.FunctionScope;
import dev.yekllurt.interpreter.interpreter.scope.ParameterScope;
import dev.yekllurt.interpreter.interpreter.scope.ReturnScope;
import dev.yekllurt.interpreter.interpreter.scope.VariableScope;
import dev.yekllurt.interpreter.interpreter.scope.impl.ParameterScopeImplementation;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
import dev.yekllurt.interpreter.interpreter.throwable.ExecutionError;
import dev.yekllurt.interpreter.interpreter.throwable.InvalidOperationError;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

@Data
@Builder
public class FunctionCallNode implements ASTNode {

    private final String functionIdentifier;
    private final ExpressionListNode arguments;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        if (NativeFunctionDirectory.isNativeFunction(functionIdentifier)) {
            var function = NativeFunctionDirectory.getNativeFunction(functionIdentifier);
            var argumentsToPass = new ArrayList<dev.yekllurt.interpreter.interpreter.scope.Data>();
            for (var argument : arguments.getExpressionList()) {
                var returnScopeExpression = new ReturnScopeImplementation();
                argument.evaluate(functionScope, variableScope, parameterScope, returnScopeExpression);
                argumentsToPass.add(returnScopeExpression.lookup());
            }
            function.execute(argumentsToPass).ifPresent(result -> returnScope.assignReturnValue(result.dataType(), result.data()));
        } else {
            var functionNode = functionScope.lookupFunction(functionIdentifier);

            if (functionNode.getParameters().size() != arguments.getExpressionList().size()) {
                throw new InvalidOperationError(String.format("Attempting to call the function '%s' with %s argument(s) however %s are required",
                        functionIdentifier, arguments.getExpressionList().size(), functionNode.getParameters().size()));
            }

            var childParameterScope = new ParameterScopeImplementation();
            for (int i = 0; i < arguments.getExpressionList().size(); i++) {
                var argument = arguments.getExpressionList().get(i);
                var parameterNode = functionNode.getParameters().get(i);

                var childReturnScope = new ReturnScopeImplementation();
                argument.evaluate(functionScope, variableScope, parameterScope, childReturnScope);

                if (!Objects.equals(childReturnScope.lookupReturnValueType(), parameterNode.getType())) {
                    throw new ExecutionError(String.format("Attempting to pass an argument of type %s however an argument of type %s is expected",
                            childReturnScope.lookupReturnValueType(), parameterNode.getType()));
                }

                // TODO: check if the return value is actually a valid value
                childParameterScope.assignData(parameterNode.getIdentifier(), parameterNode.getType(), childReturnScope.lookupReturnValue());
            }

            variableScope.beginHardScope();
            variableScope.beginSoftScope();
            var childReturnScope = new ReturnScopeImplementation();
            functionNode.evaluate(functionScope, variableScope, childParameterScope, childReturnScope);
            variableScope.endSoftScope();
            variableScope.endHardScope();

            // Only methods that do not have void as return type return a value
            if (Objects.nonNull(childReturnScope.lookup())) {
                returnScope.assignReturnValue(childReturnScope.lookupReturnValueType(), childReturnScope.lookupReturnValue());
            }
        }

    }

}
