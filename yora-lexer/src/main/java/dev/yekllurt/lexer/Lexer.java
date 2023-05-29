package dev.yekllurt.lexer;

import dev.yekllurt.lexer.token.Token;
import dev.yekllurt.lexer.token.TokenDefinition;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Lexer {

    private final String input;
    private final List<TokenDefinition> tokenDefinitionList;

    public Lexer(String input, List<TokenDefinition> tokenDefinitionList) {
        this.input = input;
        this.tokenDefinitionList = tokenDefinitionList;
    }

    public LinkedList<Token> tokenize() {
        var tokenList = new LinkedList<Token>();

        for (String line : input.split("\n")) {
            var remainingInput = line.trim();
            var tokenMatch = false;

            // TODO: update this check to >= 2 if string should be supporte as these contain whitespaces
            if (remainingInput.split("\s+").length != 2) {
                throw new LexerException(String.format("Invalid input: %s", remainingInput));
            }

            var type = remainingInput.split("\s+")[0];
            var value = remainingInput.split("\s+")[1];
            for (TokenDefinition tokenDefinition : tokenDefinitionList) {
                var pattern = Pattern.compile(tokenDefinition.getPattern());
                var matcher = pattern.matcher(value);

                if (!tokenDefinition.getCategory().equals(type) || !matcher.matches()) {
                    continue;
                }

                tokenMatch = true;
                tokenList.add(Token.builder()
                        .type(tokenDefinition.getName())
                        .value(tokenDefinition.isKeepValue() ? value : null)
                        .build());
            }

            if (!tokenMatch) {
                throw new LexerException(String.format("Invalid token '%s'", type));
            }
        }

        return tokenList;
    }

}
