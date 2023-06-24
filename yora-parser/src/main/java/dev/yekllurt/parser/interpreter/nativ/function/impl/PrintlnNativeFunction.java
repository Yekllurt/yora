package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;

import java.util.Objects;
import java.util.Optional;

public class PrintlnNativeFunction implements NativeFunction {

    /**
     * Prints the parameters to the console
     *
     * @param parameters The array of objects to print
     * @return null after print
     */
    @Override
    public Optional<Object> execute(Object... parameters) {
        if (Objects.isNull(parameters) || parameters.length != 1) {
            throw new IllegalArgumentException(String.format("The native function %s has exactly one parameter", getName()));
        }
        System.out.println(parameters[0]);
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "print";
    }

}
