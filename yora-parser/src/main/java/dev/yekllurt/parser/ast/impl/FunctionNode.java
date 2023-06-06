package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.collection.SequencedCollection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FunctionNode implements ASTNode {

    private final String identifier;
    private final String returnType;

    private final SequencedCollection<ASTNode> parameters;
    private final SequencedCollection<ASTNode> statements;
    private final ASTNode returnStatement;

}
