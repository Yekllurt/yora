package dev.yekllurt.interpreter.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {

    private final String type;
    private final String value;

}
