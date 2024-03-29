package dev.yekllurt.lexer.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenClassifierDefinition {

    private final String category;
    private final String pattern;
    private final boolean keepValue;
    private final String name;

}
