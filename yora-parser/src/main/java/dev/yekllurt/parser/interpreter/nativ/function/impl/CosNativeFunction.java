package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.utility.ParserUtility;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CosNativeFunction implements NativeFunction {

    @Override
    public Optional<Object> execute(List<Data> parameters) {
        ExceptionUtility.throwIf(Objects.isNull(parameters) || parameters.size() != 1,
                new IllegalArgumentException(String.format("The native function %s has exactly one parameter",
                        getName())));
        ExceptionUtility.throwIf(Objects.isNull(parameters.get(0)) || !ParserUtility.isNumber(parameters.get(0).dataType()),
                new IllegalArgumentException(String.format("The native function %s only accepts int or floats as parameters. Provided: %s",
                        getName(), parameters.get(0))))
        ;

        return Optional.of(Math.cos(parameters.get(0).toDouble()));
    }

    @Override
    public String getName() {
        return "cos";
    }

}
