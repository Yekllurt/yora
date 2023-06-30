package dev.yekllurt.parser.token;

import dev.yekllurt.parser.ast.throwable.ParseException;
import dev.yekllurt.api.collection.SequencedCollection;
import dev.yekllurt.parser.ast.throwable.ParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class TokenLoader {

    public SequencedCollection<Token> load(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            var tokens = new SequencedCollection<Token>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (isEmptyLine(line)) {
                    continue;
                }
                var split = line.trim().split(" ", 2);
                var type = split[0];
                var value = split.length == 2 ? split[1] : null;
                if (type.equals(TokenType.STRING)) {
                    if (Objects.isNull(value)) {
                        throw new ParseException(String.format("Attempting to load a token type %s which does not have a value", type));
                    }
                    value = value.substring(1, value.length() - 1);
                }
                tokens.add(Token.builder()
                        .type(type)
                        .value(value)
                        .build());
            }
            return tokens;
        } catch (IOException e) {
            throw new ParserException(String.format("IOException: %s", e.getMessage()));
        }
    }

    private boolean isEmptyLine(String line) {
        if (line == null) {
            return true;
        }
        return line.trim().equals("");
    }

}
