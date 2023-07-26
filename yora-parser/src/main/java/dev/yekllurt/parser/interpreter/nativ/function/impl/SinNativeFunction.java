package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.token.TokenType;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class SinNativeFunction extends NativeFunction {

    @Override
    public Optional<Data> execute(@NonNull List<Data> parameters) {
        validateArgumentCountMatch(parameters.size());

        var argument0 = parameters.get(0);
        validateArgumentDataType(0, argument0, Data::isNumber, TokenType.DECIMAL_NUMBER);

        return Optional.of(new Data(DataType.FLOAT, Math.sin(argument0.toDouble())));
    }

    @Override
    public String getName() {
        return "sin";
    }

    @Override
    public int getExpectedArgumentCount() {
        return 1;
    }

}
