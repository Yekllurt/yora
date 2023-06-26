package dev.yekllurt.scanner;

import dev.yekllurt.scanner.token.Token;
import dev.yekllurt.scanner.token.TokenDefinition;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {

    private final String input;
    private final List<TokenDefinition> tokenDefinitionList;

    public Scanner(String input, List<TokenDefinition> tokenDefinitionList) {
        this.input = input;
        this.tokenDefinitionList = tokenDefinitionList;
    }

    public List<Token> tokenize() {
        var tokenList = new LinkedList<Token>();

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
                throw new ScannerException(String.format("Invalid character '%s' at position %s", input.charAt(currentPosition), currentPosition));
            }

        }

        return tokenList;
    }

}
