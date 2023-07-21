package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.utility.ParserUtility;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PrintlnNativeFunction implements NativeFunction {

    /**
     * Prints the parameters to the console
     *
     * @param parameters The array of objects to print
     * @return null after print
     */
    @Override
    public Optional<Data> execute(List<Data> parameters) {
        ExceptionUtility.throwExceptionIf(Objects.isNull(parameters) || parameters.size() != 1,
                new IllegalArgumentException(String.format("The native function %s has exactly one parameter",
                        getName())));
        if (Objects.isNull(parameters.get(0))) {
            throw new UnsupportedOperationException("Can't output null to the console");
        }
        var data = parameters.get(0);
        if (ParserUtility.isArray(data.dataType())) {
            if (data.isStringArray()) System.out.println(Arrays.toString(data.toStringArray()));
            else if (data.isLongArray()) System.out.println(Arrays.toString(data.toLongArray()));
            else if (data.isDoubleArray()) System.out.println(Arrays.toString(data.toDoubleArray()));
            else if (data.isBooleanArray()) System.out.println(Arrays.toString(data.toBooleanArray()));
        } else {
            System.out.println(data.data());
        }
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "print";
    }

}
