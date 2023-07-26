package dev.yekllurt.api.throwable.compilation;

/**
 * An exception that is thrown when a semantic error occurs such as a type mismatch or undeclared variables
 */
public class SemanticException extends CompilationException {

    public SemanticException(String message) {
        super(message);
    }

}
