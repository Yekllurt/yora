package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.utility.ParserUtility;
import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class RandomLongNativeFunction implements NativeFunction {

    private static final Random RANDOM = new Random();

    @Override
    public Optional<Object> execute(Object... parameters) {
        if (Objects.isNull(parameters) || parameters.length != 2) {
            throw new ExecutionError("The randl function only accepts two parameter");
        }
        if (!ParserUtility.isLong(parameters[0]) || !ParserUtility.isLong(parameters[1])) {
            throw new ExecutionError("Both provided numbers must be integers");
        }
        return Optional.of(RANDOM.nextLong(ParserUtility.parseLong(parameters[0]), ParserUtility.parseLong(parameters[1]) + 1));
    }

}
