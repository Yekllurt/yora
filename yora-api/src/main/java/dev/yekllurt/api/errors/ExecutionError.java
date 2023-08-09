package dev.yekllurt.api.errors;

import dev.yekllurt.api.throwable.execution.*;
import dev.yekllurt.api.throwable.execution.ArithmeticException;

/**
 * Error code meaning: <br>
 * - 50xxx argument count mismatch errors <br>
 * - 51xxx invalid type errors <br>
 * - 52xxx arithmetic errors <br>
 * - 53xxx comparison errors <br>
 * - 54xxx conversion errors <br>
 * - 55xxx variable errors <br>
 * - 56xxx parse errors <br>
 */
public class ExecutionError {

    // === Argument count mismatch errors ===
    public static final Error INVALID_ARGUMENT_COUNT_EXPECTED_ACTUAL = new Error(50000, ArgumentCountMismatchException.class,
            "%s function expected %s arguments but found %s");

    public static final Error INVALID_ARGUMENT_COUNT_FUNCTION_CALL = new Error(50001, ArgumentCountMismatchException.class,
            "Attempting to call the function '%s' with %s argument(s) however %s are required");

    // === Invalid type errors ===
    public static final Error INVALID_TYPE_ARGUMENT_NON_NULL = new Error(51000, InvalidTypeException.class,
            "The %s function requires a value that is non null as argument %s");

    public static final Error INVALID_TYPE_ARGUMENT_NON_NULL_CUSTOM_MESSAGE = new Error(51001, InvalidTypeException.class,
            "%s");

    public static final Error INVALID_TYPE_ARGUMENT_EXPECTED_ACTUAL = new Error(51002, InvalidTypeException.class,
            "The %s function requires a value of type %s but provided a value of type %s as argument as the %s argument");

    public static final Error INVALID_TYPE_CONDITION_BOOLEAN_EXPECTED = new Error(51003, InvalidTypeException.class,
            "A condition returned the non-boolean value '%s'");

    public static final Error INVALID_TYPE_FUNCTION_PASS_PARAMETER = new Error(51004, InvalidTypeException.class,
            "Attempting to pass an argument of type %s however an argument of type %s is expected");

    public static final Error INVALID_TYPE_UNSUPPORTED_TERM = new Error(51005, InvalidTypeException.class,
            "Unable to resolve the term '%s' as it is not supported");

    // === Arithmetic errors ===
    public static final Error INVALID_ARITHMETIC_UNSUPPORTED_UNARY_OPERATION = new Error(52000, ArithmeticException.class,
            "The unary arithmetic operation '%s' is not supported");

    public static final Error INVALID_ARITHMETIC_UNSUPPORTED_OPERATION = new Error(52001, ArithmeticException.class,
            "The arithmetic operation '%s' is not supported");

    public static final Error INVALID_ARITHMETIC_UNSUPPORTED_UNARY_OPERATION_ON_DATATYPE = new Error(52002, ArithmeticException.class,
            "The unary arithmetic operation '%s' is not supported on the data type %s");

    public static final Error INVALID_ARITHMETIC_CANT_DIVIDE_BY_ZERO = new Error(52003, ArithmeticException.class,
            "Can't divide by zero");

    public static final Error INVALID_ARITHMETIC_OPERATION = new Error(52004, ArithmeticException.class,
            "Unable to %s the values '%s' and '%s' with each other, both must be %s");

    // === Comparison errors ===
    public static final Error INVALID_COMPARISON_BOTH_NULL = new Error(53000, ComparisonException.class,
            "Attempting two compare two null values");

    public static final Error INVALID_COMPARISON_NO_NUMBER = new Error(53001, ComparisonException.class,
            "Attempting to compare two values of the type %s and %s using the %s operator however both must be numbers");

    public static final Error INVALID_COMPARISON_UNSUPPORTED_OPERATION = new Error(53002, ComparisonException.class,
            "The comparison operation '%s' is not supported");

    // === Conversion errors ===
    public static final Error INVALID_CONVERSION_TYPES = new Error(54000, ConversionException.class,
            "Can't convert data from the type %s into an %s");

    public static final Error INVALID_CONVERSION_INTO_ARRAY = new Error(54001, ConversionException.class,
            "The datatype %s can't be converted into an array");

    // === Variables errors ===
    public static final Error INVALID_VARIABLE_UNSUPPORTED_DATA_TYPE_ON_DECLARATION = new Error(55000, VariableException.class,
            "Unable to declare the variable '%s' with the data type '%s' as it is not supported");

    public static final Error INVALID_VARIABLE_DECLARATION_DATA_MISS_MATCH = new Error(55001, VariableException.class,
            "Can not declare the variabel '%s' with the static type of '%s' using a value of data type '%s'");

    public static final Error INVALID_VARIABLE_EXISTS_IN_NO_SCOPE = new Error(55002, ScopeException.class,
            "Unable to resolve the variable '%s' as it doesn't exist in any of the following scopes");

    public static final Error INVALID_VARIABLE_UPDATE_INVALID_ARRAY_DATA_TYPE = new Error(55003, VariableException.class,
            "Failed updating the array of data type '%s' as it is not supported");

    public static final Error INVALID_VARIABLE_ACCESS_ARRAY_INDEX_OUT_OF_BOUNDS = new Error(55004, VariableException.class,
            "Index %s is out of bounds for length %s");

    // === Parse errors ===
    public static final Error PARSE_EXCEPTION_READ_UNSUPPORTED_DATA_TYPE = new Error(56000, ParseException.class,
            "Can't read the data type %s from the console as it is not supported");

    public static final Error PARSE_EXCEPTION_INVALID_DATA_TYPE_FOR_ARRAY_INDEX = new Error(56001, ParseException.class,
            "Unable to parse array index from '%s' as it is not an int");

    public static final Error PARSE_EXCEPTION_INVALID_DATA_TYPE_FOR_ARRAY_INDEX_AS_NO_INT_32 = new Error(56002, ParseException.class,
            "Failed parsing array index %s as when using converting it from an int64 to int32 there is an information loss");

    private ExecutionError() {
        // Intentional: other classes should not instantiate a helper class
    }

}
