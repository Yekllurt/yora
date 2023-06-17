package dev.yekllurt.parser.interpreter.nativefunction.impl;

import dev.yekllurt.parser.interpreter.nativefunction.NativeFunction;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class PrintNativeFunction implements NativeFunction {

    /**
     * Prints the parameters to the console
     *
     * @param parameters The array of objects to print
     * @return null after print
     */
    @Override
    public Optional<Object> execute(Object... parameters) {
        if (Objects.nonNull(parameters) && parameters.length == 1) {
            System.out.println(parameters[0]);
        } else {
            System.out.println(Arrays.toString(parameters));
        }
        return Optional.empty();
    }

}
