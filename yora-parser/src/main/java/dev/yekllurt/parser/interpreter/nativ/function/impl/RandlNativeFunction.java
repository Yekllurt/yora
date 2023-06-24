package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.utility.ParserUtility;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class RandlNativeFunction implements NativeFunction {

    private static final Random RANDOM = new Random();

    @Override
    public Optional<Object> execute(Object... parameters) {
        if (Objects.isNull(parameters) || parameters.length != 2) {
            throw new IllegalArgumentException(String.format("The native function %s has exactly two parameters", getName()));
        }
        if (!ParserUtility.isLong(parameters[0]) || !ParserUtility.isLong(parameters[1])) {
            throw new IllegalArgumentException(String.format("The native function %s only accepts int or floats as parameters. Provided: %s, %s", getName(), parameters[0], parameters[1]));
        }
        return Optional.of(RANDOM.nextLong(ParserUtility.parseLong(parameters[0]), ParserUtility.parseLong(parameters[1]) + 1));
    }

    @Override
    public String getName() {
        return "randl";
    }

}
