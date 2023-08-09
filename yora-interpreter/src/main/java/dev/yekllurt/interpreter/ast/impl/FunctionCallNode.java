package dev.yekllurt.interpreter.ast.impl;

import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.interpreter.interpreter.nativ.function.NativeFunctionDirectory;
import dev.yekllurt.interpreter.interpreter.scope.FunctionScope;
import dev.yekllurt.interpreter.interpreter.scope.ParameterScope;
import dev.yekllurt.interpreter.interpreter.scope.ReturnScope;
import dev.yekllurt.interpreter.interpreter.scope.VariableScope;
import dev.yekllurt.interpreter.interpreter.scope.impl.ParameterScopeImplementation;
import dev.yekllurt.interpreter.interpreter.scope.impl.ReturnScopeImplementation;
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

            ExceptionUtility.throwExceptionIf(functionNode.getParameters().size() != arguments.getExpressionList().size(),
                    dev.yekllurt.api.errors.ExecutionError.INVALID_ARGUMENT_COUNT_FUNCTION_CALL,
                    functionIdentifier, arguments.getExpressionList().size(), functionNode.getParameters().size());

            var childParameterScope = new ParameterScopeImplementation();
            for (int i = 0; i < arguments.getExpressionList().size(); i++) {
                var argument = arguments.getExpressionList().get(i);
                var parameterNode = functionNode.getParameters().get(i);

                var childReturnScope = new ReturnScopeImplementation();
                argument.evaluate(functionScope, variableScope, parameterScope, childReturnScope);

                ExceptionUtility.throwExceptionIf(!Objects.equals(childReturnScope.lookupReturnValueType(), parameterNode.getType()),
                        dev.yekllurt.api.errors.ExecutionError.INVALID_TYPE_FUNCTION_PASS_PARAMETER,
                        childReturnScope.lookupReturnValueType(), parameterNode.getType());

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
