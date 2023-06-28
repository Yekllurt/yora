package dev.yekllurt.lexer;

import dev.yekllurt.api.collection.SequencedCollection;
import dev.yekllurt.api.token.Token;
import dev.yekllurt.lexer.throwable.LexerException;
import dev.yekllurt.lexer.token.TokenClassifierDefinition;

import java.util.List;
import java.util.regex.Pattern;

public class TokenClassifier {

    private final SequencedCollection<Token> tokens;
    private final List<TokenClassifierDefinition> tokenClassifierDefinitionList;

    public TokenClassifier(SequencedCollection<Token> tokens, List<TokenClassifierDefinition> tokenClassifierDefinitionList) {
        this.tokens = tokens;
        this.tokenClassifierDefinitionList = tokenClassifierDefinitionList;
    }

    public SequencedCollection<Token> tokenize() {
        var tokenList = new SequencedCollection<Token>();

        for (var token : tokens) {
            var tokenMatch = false;

            var type = token.getType();
            var value = token.getValue();

            for (var tokenDefinition : tokenClassifierDefinitionList) {
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
