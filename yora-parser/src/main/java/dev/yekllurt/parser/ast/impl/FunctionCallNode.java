package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FunctionCallNode implements ASTNode {

    private final String functionIdentifier;
    private final ASTNode arguments;

}
