package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;

import java.util.Objects;
import java.util.Optional;

public class SinNativeFunction implements NativeFunction {

    @Override
    public Optional<Object> execute(Object... parameters) {
        if (Objects.isNull(parameters) || parameters.length != 1) {
            throw new ExecutionError("The sin function only accepts one parameter");
        }
        return Optional.of(Math.sin(Double.parseDouble(String.valueOf(parameters[0]))));
    }

}
