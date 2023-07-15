package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.interpreter.scope.Data;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class RandlNativeFunction implements NativeFunction {

    private static final Random RANDOM = new Random();

    @Override
    public Optional<Data> execute(List<Data> parameters) {
        ExceptionUtility.throwIf(Objects.isNull(parameters) || parameters.size() != 2,
                new IllegalArgumentException(String.format("The native function %s has exactly two parameters",
                        getName())));
        ExceptionUtility.throwIf(Objects.isNull(parameters.get(0)) || Objects.isNull(parameters.get(1))
                        || !parameters.get(0).isLong() || !parameters.get(1).isLong(),
                new IllegalArgumentException(String.format("The native function %s only accepts int as parameters. Provided: %s, %s",
                        getName(), parameters.get(0), parameters.get(1))))
        ;

        return Optional.of(new Data(DataType.INT, RANDOM.nextLong(parameters.get(0).toLong(), parameters.get(1).toLong() + 1)));
    }

    @Override
    public String getName() {
        return "randl";
    }

}
