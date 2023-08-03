package dev.yekllurt.interpreter.interpreter.nativ.function.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.interpreter.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.interpreter.interpreter.scope.Data;
import dev.yekllurt.interpreter.token.TokenType;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class CosNativeFunction extends NativeFunction {

    @Override
    public Optional<Data> execute(@NonNull List<Data> parameters) {
        validateArgumentCountMatch(parameters.size());

        var argument0 = parameters.get(0);
        validateArgumentDataType(0, argument0, Data::isNumber, TokenType.DECIMAL_NUMBER);

        return Optional.of(new Data(DataType.FLOAT, Math.cos(argument0.toDouble())));
    }

    @Override
    public String getName() {
        return "cos";
    }

    @Override
    public int getExpectedArgumentCount() {
        return 1;
    }

}
