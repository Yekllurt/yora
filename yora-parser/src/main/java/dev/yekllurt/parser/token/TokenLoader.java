package dev.yekllurt.parser.token;

import dev.yekllurt.parser.collection.SequencedCollection;
import dev.yekllurt.parser.ast.throwable.exception.ParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
                tokens.add(Token.builder()
                        .type(split[0])
                        .value(split.length == 2 ? split[1] : null)
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
