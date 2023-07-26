package dev.yekllurt.api.throwable.compilation;

/**
 * An exception that is thrown during the compilation process
 */
public class CompilationException extends RuntimeException {

    public CompilationException(String message) {
        super(message);
    }

}
