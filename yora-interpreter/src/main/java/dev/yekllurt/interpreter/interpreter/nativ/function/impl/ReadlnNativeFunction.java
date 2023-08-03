package dev.yekllurt.interpreter.interpreter.nativ.function.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.interpreter.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.interpreter.interpreter.scope.Data;
import dev.yekllurt.interpreter.utility.ParserUtility;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class ReadlnNativeFunction extends NativeFunction {

    /**
     * Reads data from the console
     *
     * @param parameters An array containing the data type to be read as well as an error message
     * @return the value read from the console
     */
    @Override
    public Optional<Data> execute(@NonNull List<Data> parameters) {
        validateArgumentCountMatch(parameters.size());

        var argument0 = parameters.get(0);
        validateArgumentNonNull(argument0, 0);
        var argument1 = parameters.get(1);
        validateArgumentNonNull(argument1, 1);

        var dataTypeStr = argument0.toString().toUpperCase();
        var dataType = DataType.fromString(dataTypeStr, false);
        validateArgumentNonNull(dataType, "The data type %s is not supported as console input".formatted(dataTypeStr));

        var errorMessage = argument1.toString();

        boolean success = false;
        String userInput = null;
        DataType userInputDataType = null;
        while (!success) {
            userInput = readConsoleLine();
            userInputDataType = ParserUtility.getReturnType(userInput);
            if (DataType.INT.equals(userInputDataType) && DataType.FLOAT.equals(dataType)) {
                userInputDataType = DataType.FLOAT;
            }
            if (!dataType.equals(userInputDataType)) {
                System.err.println(errorMessage);
                continue;
            }
            success = true;
        }
        Object result;
        switch (userInputDataType) {
            case INT -> result = ParserUtility.parseLong(userInput);
            case FLOAT -> result = ParserUtility.parseDouble(userInput);
            case STRING -> result = userInput;
            default ->
                    throw new UnsupportedOperationException(String.format("Can't read the data type %s from the console as it is not supported", userInputDataType));
        }
        return Optional.of(new Data(userInputDataType, result));
    }

    @Override
    public String getName() {
        return "print";
    }

    @Override
    public int getExpectedArgumentCount() {
        return 2;
    }

    private String readConsoleLine() {
        // The program is executed via the java command
        if (Objects.nonNull(System.console())) {
            return System.console().readLine();
        }
        // The program is executed in an IDE
        return new Scanner(System.in).nextLine();
    }

}
