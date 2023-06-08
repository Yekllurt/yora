package dev.yekllurt.parser.ast.throwable.exception;

/**
 * An exception that is thrown by the parser when it attempts to handle a token that it doesn't recognize in other
 * words is not defined
 */
public class UnsupportedTokenTypeException extends RuntimeException {

    public UnsupportedTokenTypeException(String message) {
        super(message);
    }

}
