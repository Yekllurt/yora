package dev.yekllurt.api.throwable.compilation;

/**
 * An exception that is thrown when converting text into tokens fails
 */
public class LexicalException extends CompilationException {

    public LexicalException(String message) {
        super(message);
    }

}
