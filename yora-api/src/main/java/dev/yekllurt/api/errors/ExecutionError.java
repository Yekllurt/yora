package dev.yekllurt.api.errors;

import dev.yekllurt.api.throwable.execution.*;
import dev.yekllurt.api.throwable.execution.ArithmeticException;

public class ExecutionError {

    // === ArgumentCountMismatch errors ===
    public static final Error INVALID_ARGUMENT_COUNT_EXPECTED_ACTUAL = new Error(-1, ArgumentCountMismatchException.class,
            "%s function expected %s arguments but found %s");

    public static final Error INVALID_TYPE_ARGUMENT_NON_NULL = new Error(-1, InvalidTypeArgumentException.class,
            "The %s function requires a value that is non null as argument %s");

    public static final Error INVALID_TYPE_ARGUMENT_NON_NULL_CUSTOM_MESSAGE = new Error(-1, InvalidTypeArgumentException.class,
            "%s");

    public static final Error INVALID_TYPE_ARGUMENT_EXPECTED_ACTUAL = new Error(-1, InvalidTypeArgumentException.class,
            "The %s function requires a value of type %s but provided a value of type %s as argument as the %s argument");

    // === Arithmetic errors ===
    public static final Error INVALID_ARITHMETIC_UNSUPPORTED_UNARY_OPERATION = new Error(-1, ArithmeticException.class,
            "The unary arithmetic operation '%s' is not supported");

    public static final Error INVALID_ARITHMETIC_UNSUPPORTED_OPERATION = new Error(-1, ArithmeticException.class,
            "The arithmetic operation '%s' is not supported");

    public static final Error INVALID_ARITHMETIC_UNSUPPORTED_UNARY_OPERATION_ON_DATATYPE = new Error(-1, ArithmeticException.class,
            "The unary arithmetic operation '%s' is not supported on the data type %s");

    public static final Error INVALID_ARITHMETIC_CANT_DIVIDE_BY_ZERO = new Error(-1, ArithmeticException.class,
            "Can't divide by zero");

    public static final Error INVALID_ARITHMETIC_OPERATION = new Error(-1, ArithmeticException.class,
            "Unable to %s the values '%s' and '%s' with each other, both must be %s");

    // === Comparison errors ===
    public static final Error INVALID_COMPARISON_BOTH_NULL = new Error(-1, ComparisonException.class,
            "Attempting two compare two null values");

    public static final Error INVALID_COMPARISON_NO_NUMBER = new Error(-1, ComparisonException.class,
            "Attempting to compare two values of the type %s and %s using the %s operator however both must be numbers");

    public static final Error INVALID_COMPARISON_UNSUPPORTED_OPERATION = new Error(-1, ComparisonException.class,
            "The comparison operation '%s' is not supported");

    // === Conversion errors ===
    public static final Error INVALID_CONVERSION_TYPES = new Error(-1, ConversionException.class,
            "Can't convert data from the type %s into an %s");

    public static final Error INVALID_CONVERSION_INTO_ARRAY = new Error(-1, ConversionException.class,
            "The datatype %s can't be converted into an array");

    // === Variables errors ===
    public static final Error INVALID_VARIABLE_UNSUPPORTED_DATA_TYPE_ON_DECLARATION = new Error(-1, VariableException.class,
            "Unable to declare the variable '%s' with the data type '%s' as it is not supported");

    public static final Error INVALID_VARIABLE_DECLARATION_DATA_MISS_MATCH = new Error(-1, VariableException.class,
            "Can not declare the variabel '%s' with the static type of '%s' using a value of data type '%s'");

    // Can't read the data type %s from the console as it is not supported
    public static final Error PARSE_EXCEPTION_READ_UNSUPPORTED_DATA_TYPE = new Error(-1, ParseException.class,
            "Can't read the data type %s from the console as it is not supported");

    private ExecutionError() {
        // Intentional: other classes should not instantiate a helper class
    }

}
