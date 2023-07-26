package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.token.TokenType;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandlNativeFunction extends NativeFunction {

    private static final Random RANDOM = new Random();

    @Override
    public Optional<Data> execute(@NonNull List<Data> parameters) {
        validateArgumentCountMatch(parameters.size());

        var argument0 = parameters.get(0);
        validateArgumentDataType(0, argument0, Data::isLong, TokenType.DECIMAL_NUMBER);
        var argument1 = parameters.get(1);
        validateArgumentDataType(1, argument1, Data::isLong, TokenType.DECIMAL_NUMBER);

        return Optional.of(new Data(DataType.INT, RANDOM.nextLong(argument0.toLong(), argument1.toLong() + 1)));
    }

    @Override
    public String getName() {
        return "randl";
    }

    @Override
    public int getExpectedArgumentCount() {
        return 2;
    }

}
