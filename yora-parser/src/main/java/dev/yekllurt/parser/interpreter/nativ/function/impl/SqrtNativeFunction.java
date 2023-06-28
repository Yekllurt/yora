package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.utility.ParserUtility;

import java.util.Objects;
import java.util.Optional;

public class SqrtNativeFunction implements NativeFunction {

    @Override
    public Optional<Object> execute(Object... parameters) {
        ExceptionUtility.throwIf(Objects.isNull(parameters) || parameters.length != 1,
                new IllegalArgumentException(String.format("The native function %s has exactly one parameter", getName())));
        ExceptionUtility.throwIf(!ParserUtility.isLong(parameters[0]),
                new IllegalArgumentException(String.format("The native function %s only accepts int as parameter. Provided: %s", getName(), parameters[0])));

        return Optional.of(Math.sqrt(Double.parseDouble(String.valueOf(parameters[0]))));
    }

    @Override
    public String getName() {
        return "sqrt";
    }

}
