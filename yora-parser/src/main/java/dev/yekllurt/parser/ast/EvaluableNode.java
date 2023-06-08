package dev.yekllurt.parser.ast;

import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;

public interface EvaluableNode {

    void evaluate(VariableScope variableScope, ParameterScope parameterScope, ReturnScope returnScope);

}