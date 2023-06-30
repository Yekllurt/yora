package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.interpreter.scope.Data;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SqrtNativeFunction implements NativeFunction {

    @Override
    public Optional<Object> execute(List<Data> parameters) {
        ExceptionUtility.throwIf(Objects.isNull(parameters) || parameters.size() != 1,
                new IllegalArgumentException(String.format("The native function %s has exactly one parameter",
                        getName())));
        ExceptionUtility.throwIf(Objects.isNull(parameters.get(0)) || !parameters.get(0).isLong(),
                new IllegalArgumentException(String.format("The native function %s only accepts int as parameter. Provided: %s",
                        getName(), parameters.get(0))));

        return Optional.of(Math.sqrt(Double.parseDouble(String.valueOf(parameters.get(0)))));
    }

    @Override
    public String getName() {
        return "sqrt";
    }

}
