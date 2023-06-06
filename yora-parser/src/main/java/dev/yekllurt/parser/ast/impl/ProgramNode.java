package dev.yekllurt.parser.ast.impl;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.collection.SequencedCollection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgramNode implements ASTNode {

    private final SequencedCollection<ASTNode> functions;

}
