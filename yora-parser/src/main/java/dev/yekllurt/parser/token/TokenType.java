package dev.yekllurt.parser.token;

public class TokenType {

    public static final String KEYWORD_INT = "INT";
    public static final String KEYWORD_FLOAT = "FLOAT";
    public static final String KEYWORD_BOOLEAN = "BOOLEAN";
    public static final String KEYWORD_CHAR = "CHAR";
    public static final String KEYWORD_VOID = "VOID";
    public static final String KEYWORD_END = "END";
    public static final String KEYWORD_RETURN = "RETURN";
    public static final String KEYWORD_IF = "IF";

    public static final String PUNCTUATION_LEFT_BRACE = "LEFT_BRACE";
    public static final String PUNCTUATION_RIGHT_BRACE = "RIGHT_BRACE";
    public static final String PUNCTUATION_SEMICOLON = "SEMICOLON";
    public static final String PUNCTUATION_COMMA = "COMMA";
    public static final String PUNCTUATION_PLUS = "PLUS";
    public static final String PUNCTUATION_MINUS = "MINUS";
    public static final String PUNCTUATION_STAR = "STAR";
    public static final String PUNCTUATION_DIVIDE = "DIVIDE";
    public static final String PUNCTUATION_EQUAL = "EQUAL";
    public static final String PUNCTUATION_EXCLAMATION_MARK = "EXCLAMATION_MARK";
    public static final String PUNCTUATION_CARET = "CARET";

    public static final String IDENTIFIER = "IDENTIFIER";
    public static final String DECIMAL_NUMBER = "NUMBER";

    private TokenType() {
        // Intentional as a helper class should not be instantiated by other classes
    }

}
