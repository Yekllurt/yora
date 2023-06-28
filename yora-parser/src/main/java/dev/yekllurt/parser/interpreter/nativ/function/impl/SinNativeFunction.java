package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.utility.ParserUtility;

import java.util.Objects;
import java.util.Optional;

public class SinNativeFunction implements NativeFunction {

    @Override
    public Optional<Object> execute(Object... parameters) {
        ExceptionUtility.throwIf(Objects.isNull(parameters) || parameters.length != 1,
                new IllegalArgumentException(String.format("The native function %s has exactly one parameter", getName())));
        ExceptionUtility.throwIf(!(ParserUtility.isLong(parameters[0]) || ParserUtility.isDouble(parameters[0])),
                new IllegalArgumentException(String.format("The native function %s only accepts int or floats as parameters. Provided: %s", getName(), parameters[0])));

        return Optional.of(Math.sin(Double.parseDouble(String.valueOf(parameters[0]))));
    }

    @Override
    public String getName() {
        return "sin";
    }

}
