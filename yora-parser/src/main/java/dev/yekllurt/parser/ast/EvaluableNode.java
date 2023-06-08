package dev.yekllurt.parser.ast;

import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;

/**
 * Make a node evaluable, in other words it can be interpreted by an interpreter
 */
public interface EvaluableNode {

    void evaluate(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope);

}