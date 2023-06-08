package dev.yekllurt.parser.interpreter.nativefunction;

/**
 * An interface describing basic functions that are provided by the programming language
 */
public interface NativeFunction {

    /**
     * The implementation of the function
     */
    Object execute(Object... parameters);

}
