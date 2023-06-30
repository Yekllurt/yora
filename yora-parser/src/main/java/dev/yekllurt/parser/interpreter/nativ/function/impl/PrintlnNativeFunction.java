package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.interpreter.scope.Data;

import java.util.List;
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
    public Optional<Object> execute(List<Data> parameters) {
        ExceptionUtility.throwIf(Objects.isNull(parameters) || parameters.size() != 1,
                new IllegalArgumentException(String.format("The native function %s has exactly one parameter",
                        getName())));

        System.out.println(Objects.isNull(parameters.get(0)) ? null : parameters.get(0).data());
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "print";
    }

}
