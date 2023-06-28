package dev.yekllurt.lexer;

import dev.yekllurt.api.collection.SequencedCollection;
import dev.yekllurt.api.token.Token;
import dev.yekllurt.lexer.throwable.LexerException;
import dev.yekllurt.lexer.token.TokenScanDefinition;

import java.util.List;
import java.util.regex.Pattern;

public class TokenScanner {

    private final String input;
    private final List<TokenScanDefinition> tokenDefinitionList;

    public TokenScanner(String input, List<TokenScanDefinition> tokenDefinitionList) {
        this.input = input;
        this.tokenDefinitionList = tokenDefinitionList;
    }

    public SequencedCollection<Token> tokenize() {
        var tokenList = new SequencedCollection<Token>();

        var currentPosition = 0;
        while (currentPosition < input.length()) {

            var remainingInput = input.substring(currentPosition);
            var tokenMatch = false;
            for (var tokenDefinition : tokenDefinitionList) {
                var pattern = Pattern.compile(tokenDefinition.getPattern());
                var matcher = pattern.matcher(remainingInput);

                if (matcher.lookingAt()) {
                    var lexeme = matcher.group();
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
