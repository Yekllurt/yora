package dev.yekllurt.parser.interpreter.nativefunction.impl;

import dev.yekllurt.parser.interpreter.nativefunction.NativeFunction;
import dev.yekllurt.parser.interpreter.throwable.error.ExecutionError;

import java.util.Objects;

public class SqrtNativeFunction implements NativeFunction {

    @Override
    public Object execute(Object... parameters) {
        if (Objects.isNull(parameters) || parameters.length != 1) {
            throw new ExecutionError("The sqrt function only accepts one parameter");
        }
        return (float) Math.sqrt((float) parameters[0]);
    }

}
