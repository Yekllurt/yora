package dev.yekllurt.api.throwable;

/**
 * An exception that is thrown when during the creating of an AST (abstract syntax tree) invalid grammar occurs
 */
public class SyntaxException extends CompilationException {

    public SyntaxException(String message) {
        super(message);
    }

}
