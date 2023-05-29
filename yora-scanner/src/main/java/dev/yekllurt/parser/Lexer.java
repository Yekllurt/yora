package dev.yekllurt.parser;

import dev.yekllurt.parser.token.Token;
import dev.yekllurt.parser.token.TokenDefinition;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final String input;
    private final List<TokenDefinition> tokenDefinitionList;

    public Lexer(String input, List<TokenDefinition> tokenDefinitionList) {
        this.input = input;
        this.tokenDefinitionList = tokenDefinitionList;
    }

    public List<Token> tokenize() {
        List<Token> tokenList = new LinkedList<>();

        int currentPosition = 0;
        while (currentPosition < input.length()) {

            String remainingInput = input.substring(currentPosition);
            boolean tokenMatch = false;
            for (TokenDefinition tokenDefinition : tokenDefinitionList) {
                Pattern pattern = Pattern.compile(tokenDefinition.getPattern());
                Matcher matcher = pattern.matcher(remainingInput);

                if (matcher.lookingAt()) {
                    String lexeme = matcher.group();
                    currentPosition += lexeme.length();
                    if (!tokenDefinition.isIgnore()) {
                        tokenList.add(Token.builder()
                                .type(tokenDefinition.getName())
                                .value(lexeme)
                                .build());
                    }
                    tokenMatch = true;
                    break;
                }
            }

            if (!tokenMatch) {
                throw new LexerException(String.format("Invalid character '%s' at position %s", input.charAt(currentPosition), currentPosition));
            }

        }

        return tokenList;
    }

}
