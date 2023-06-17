package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;

import java.util.Objects;
import java.util.Optional;

public class SqrtNativeFunction implements NativeFunction {

    @Override
    public Optional<Object> execute(Object... parameters) {
        if (Objects.isNull(parameters) || parameters.length != 1) {
            throw new ExecutionError("The sqrt function only accepts one parameter");
        }
        return Optional.of((float) Math.sqrt((float) parameters[0]));
    }

}
