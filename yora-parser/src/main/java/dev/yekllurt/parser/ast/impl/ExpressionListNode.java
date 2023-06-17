package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.collection.SequencedCollection;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpressionListNode implements ASTNode {

    private final SequencedCollection<ASTNode> expressionList;

    @Override
    public void evaluate(FunctionScope functionScope, VariableScope variableScope,
                         ParameterScope parameterScope, ReturnScope returnScope) {
        for (ASTNode expression : expressionList) {
            expression.evaluate(functionScope, variableScope, null, null); // handle here returnScope=null
        }
    }

    public SequencedCollection<ASTNode> getExpressionList() {
        return expressionList;
    }

}
