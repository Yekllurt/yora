package dev.yekllurt.api.errors;

import dev.yekllurt.api.throwable.execution.ArgumentCountMismatchException;
import dev.yekllurt.api.throwable.execution.ArithmeticException;
import dev.yekllurt.api.throwable.execution.ComparisonException;
import dev.yekllurt.api.throwable.execution.InvalidTypeArgumentException;

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
    public static final Error INVALID_ARITHMETIC_UNSUPPORTED_OPERATION = new Error(-1, ArithmeticException.class,
            "The arithmetic operation '%s' is not supported");

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

    private ExecutionError() {
        // Intentional: other classes should not instantiate a helper class
    }

}
