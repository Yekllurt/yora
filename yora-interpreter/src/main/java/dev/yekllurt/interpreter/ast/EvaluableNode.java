package dev.yekllurt.interpreter.ast;

import dev.yekllurt.interpreter.interpreter.scope.FunctionScope;
import dev.yekllurt.interpreter.interpreter.scope.ParameterScope;
import dev.yekllurt.interpreter.interpreter.scope.ReturnScope;
import dev.yekllurt.interpreter.interpreter.scope.VariableScope;

/**
 * Make a node evaluable, in other words it can be interpreted by an interpreter
 */
public interface EvaluableNode {

    void evaluate(FunctionScope functionScope,
                  VariableScope variableScope,
                  ParameterScope parameterScope,
                  ReturnScope returnScope);

}