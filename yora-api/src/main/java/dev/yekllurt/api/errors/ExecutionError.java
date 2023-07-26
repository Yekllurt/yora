package dev.yekllurt.api.errors;

import dev.yekllurt.api.throwable.execution.ArgumentCountMismatchException;
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

    private ExecutionError() {
        // Intentional: other classes should not instantiate a helper class
    }

}
