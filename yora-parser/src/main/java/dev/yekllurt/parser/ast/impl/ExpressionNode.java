package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpressionNode implements ASTNode {

    private final ASTNode left;
    private final ASTNode right;
    private final String operator;

}
