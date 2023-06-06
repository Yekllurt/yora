package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReturnNode implements ASTNode {

    private final ASTNode value;

}
