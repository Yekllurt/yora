package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;
import dev.yekllurt.parser.utility.ParserUtility;

import java.util.Objects;
import java.util.Optional;

public class SqrtNativeFunction implements NativeFunction {

    @Override
    public Optional<Object> execute(Object... parameters) {
        if (Objects.isNull(parameters) || parameters.length != 1) {
            throw new IllegalArgumentException(String.format("The native function %s has exactly one parameter", getName()));
        }
        if (!ParserUtility.isLong(parameters[0])) {
            throw new IllegalArgumentException(String.format("The native function %s only accepts int as parameter. Provided: %s", getName(), parameters[0]));
        }
        return Optional.of(Math.sqrt(Double.parseDouble(String.valueOf(parameters[0]))));
    }

    @Override
    public String getName() {
        return "sqrt";
    }

}
