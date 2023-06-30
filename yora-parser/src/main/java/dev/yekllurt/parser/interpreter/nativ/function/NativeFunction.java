package dev.yekllurt.parser.interpreter.nativ.function;

import dev.yekllurt.parser.interpreter.scope.Data;

import java.util.List;
import java.util.Optional;

/**
 * An interface describing basic functions that are provided by the programming language
 */
public interface NativeFunction {

    /**
     * The implementation of the function
     */
    Optional<Object> execute(List<Data> parameters);

    /**
     * The function name
     *
     * @return the function name
     */
    String getName();

}
