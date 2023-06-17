package dev.yekllurt.parser.interpreter.nativ.function;

import java.util.Optional;

/**
 * An interface describing basic functions that are provided by the programming language
 */
public interface NativeFunction {

    /**
     * The implementation of the function
     */
    Optional<Object> execute(Object... parameters);

}
