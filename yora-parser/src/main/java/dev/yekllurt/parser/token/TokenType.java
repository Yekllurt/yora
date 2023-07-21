package dev.yekllurt.parser.token;

import java.util.Set;

public class TokenType {

    // === Token categories ===
    public static final Set<String> ADDITIVE_TYPES = Set.of(TokenType.PUNCTUATION_PLUS, TokenType.PUNCTUATION_MINUS);
    public static final Set<String> MULTIPLICATIVE_TYPES = Set.of(TokenType.PUNCTUATION_STAR, TokenType.PUNCTUATION_DIVIDE, TokenType.PUNCTUATION_PERCENT);

    public static final Set<String> VARIABLE_TYPES = Set.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT,
            TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_STRING);
    public static final Set<String> METHOD_RETURN_TYPES = Set.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT,
            TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_STRING,
            TokenType.KEYWORD_VOID);

    public static final Set<String> TYPES_WITH_VALUES = Set.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT,
            TokenType.KEYWORD_BOOLEAN, TokenType.STRING);

    public static final Set<String> TYPES_WITHOUT_VALUES = Set.of(TokenType.KEYWORD_VOID);


    // === Tokens ===
    // = Keywords =
    public static final String KEYWORD_INT = "INT";
    public static final String KEYWORD_FLOAT = "FLOAT";
    public static final String KEYWORD_STRING = "STR";
    public static final String KEYWORD_BOOLEAN = "BOOL";
    public static final String KEYWORD_VOID = "VOID";
    public static final String KEYWORD_END = "END";
    public static final String KEYWORD_RETURN = "RETURN";
    public static final String KEYWORD_IF = "IF";
    public static final String KEYWORD_ELSE = "ELSE";
    public static final String KEYWORD_WHILE = "WHILE";
    public static final String KEYWORD_TRUE = "TRUE";
    public static final String KEYWORD_FALSE = "FALSE";

    // = Punctuation =
    public static final String PUNCTUATION_LEFT_BRACE = "LEFT_BRACE";
    public static final String PUNCTUATION_RIGHT_BRACE = "RIGHT_BRACE";
    public static final String PUNCTUATION_LEFT_BRACKET = "LEFT_BRACKET";
    public static final String PUNCTUATION_RIGHT_BRACKET = "RIGHT_BRACKET";
    public static final String PUNCTUATION_SEMICOLON = "SEMICOLON";
    public static final String PUNCTUATION_COMMA = "COMMA";
    public static final String PUNCTUATION_PLUS = "PLUS";
    public static final String PUNCTUATION_MINUS = "MINUS";
    public static final String PUNCTUATION_STAR = "STAR";
    public static final String PUNCTUATION_DIVIDE = "DIVIDE";
    public static final String PUNCTUATION_EQUAL = "EQUAL";
    public static final String PUNCTUATION_EXCLAMATION_MARK = "EXCLAMATION_MARK";
    public static final String PUNCTUATION_CARET = "CARET";
    public static final String PUNCTUATION_GREATER_THAN = "GREATER_THAN";
    public static final String PUNCTUATION_LESS_THAN = "LESS_THAN";
    public static final String PUNCTUATION_PERCENT = "PERCENT";
    public static final String PUNCTUATION_AND = "AND";
    public static final String PUNCTUATION_OR = "OR";

    // = Tokens with values =
    public static final String IDENTIFIER = "IDENTIFIER";
    public static final String DECIMAL_NUMBER = "NUMBER";
    public static final String STRING = "STRING";

    private TokenType() {
        // Intentional as a helper class should not be instantiated by other classes
    }

}
