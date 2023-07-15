package dev.yekllurt.parser.interpreter.nativ.function.impl;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.interpreter.nativ.function.NativeFunction;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.utility.ParserUtility;

import java.util.*;

public class ReadlnNativeFunction implements NativeFunction {

    /**
     * Prints the parameters to the console
     *
     * @param parameters The array of objects to print
     * @return null after print
     */
    @Override
    public Optional<Data> execute(List<Data> parameters) {
        ExceptionUtility.throwIf(Objects.isNull(parameters) || parameters.size() != 2,
                new IllegalArgumentException(String.format("The native function %s has exactly two parameters",
                        getName())));
        ExceptionUtility.throwIf(Objects.isNull(parameters.get(0)),
                new UnsupportedOperationException("Can't read an undefined data type from the console"));
        ExceptionUtility.throwIf(Objects.isNull(parameters.get(1)),
                new UnsupportedOperationException("Can't read data from the console if not error message is present"));

        var dataTypeStr = parameters.get(0).toString().toUpperCase();
        var dataType = DataType.fromString(dataTypeStr, false);
        ExceptionUtility.throwIf(Objects.isNull(dataType),
                new IllegalArgumentException("The data type %s is not supported as console input"));
        var errorMessage = parameters.get(1).toString();

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
        Object result = null;
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

    private String readConsoleLine() {
        // The program is executed via the java command
        if (Objects.nonNull(System.console())) {
            return System.console().readLine();
        }
        // The program is executed in an IDE
        return new Scanner(System.in).nextLine();
    }

}
