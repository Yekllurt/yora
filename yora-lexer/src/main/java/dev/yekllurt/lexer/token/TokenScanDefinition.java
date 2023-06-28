package dev.yekllurt.lexer.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenScanDefinition {

    private final String name;
    private final String pattern;
    private final boolean ignore;

}
