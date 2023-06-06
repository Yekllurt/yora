package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignmentNode implements ASTNode {

    private final String identifier;
    private final ASTNode value;

}
