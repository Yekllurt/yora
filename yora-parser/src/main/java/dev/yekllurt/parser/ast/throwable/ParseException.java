package dev.yekllurt.parser.ast.throwable;

/**
 * An exception that is thrown when attempting to convert/parse a term into something that it isn't
 */
public class ParseException extends RuntimeException {

    public ParseException(String message) {
        super(message);
    }

}
