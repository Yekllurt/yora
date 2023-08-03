package dev.yekllurt.interpreter.interpreter.nativ.function.impl;

import dev.yekllurt.interpreter.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.interpreter.interpreter.scope.Data;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PrintlnNativeFunction extends NativeFunction {

    /**
     * Prints the parameters to the console
     *
     * @param parameters The array of objects to print
     * @return null after print
     */
    @Override
    public Optional<Data> execute(@NonNull List<Data> parameters) {
        validateArgumentCountMatch(parameters.size());

        var argument0 = parameters.get(0);
        validateArgumentNonNull(argument0, 0);

        if (argument0.isArray()) {
            if (argument0.isStringArray()) System.out.println(Arrays.toString(argument0.toStringArray()));
            else if (argument0.isLongArray()) System.out.println(Arrays.toString(argument0.toLongArray()));
            else if (argument0.isDoubleArray()) System.out.println(Arrays.toString(argument0.toDoubleArray()));
            else if (argument0.isBooleanArray()) System.out.println(Arrays.toString(argument0.toBooleanArray()));
        } else {
            System.out.println(argument0.data());
        }
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "print";
    }

    @Override
    public int getExpectedArgumentCount() {
        return 1;
    }

}
