package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.utility.ParserUtility;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SinNativeFunction implements NativeFunction {

    @Override
    public Optional<Data> execute(List<Data> parameters) {
        ExceptionUtility.throwIf(Objects.isNull(parameters) || parameters.size() != 1,
                new IllegalArgumentException(String.format("The native function %s has exactly one parameter",
                        getName())));
        ExceptionUtility.throwIf(Objects.isNull(parameters.get(0)) || !ParserUtility.isNumber(parameters.get(0).dataType()),
                new IllegalArgumentException(String.format("The native function %s only accepts int or floats as parameters. Provided: %s",
                        getName(), parameters.get(0))));

        return Optional.of(new Data(DataType.FLOAT, Math.sin(Double.parseDouble(String.valueOf(parameters.get(0))))));
    }

    @Override
    public String getName() {
        return "sin";
    }

}
