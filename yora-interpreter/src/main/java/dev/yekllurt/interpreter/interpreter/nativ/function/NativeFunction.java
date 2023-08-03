package dev.yekllurt.interpreter.interpreter.nativ.function;

import dev.yekllurt.api.errors.ExecutionError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.interpreter.interpreter.scope.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * An interface describing basic functions that are provided by the programming language
 */
public abstract class NativeFunction {

    /**
     * The implementation of the function
     */
    public abstract Optional<Data> execute(@NonNull List<Data> parameters);

    /**
     * The function name
     *
     * @return the function name
     */
    public abstract String getName();

    /**
     * The function argument count
     *
     * @return the function argument count
     */
    public abstract int getExpectedArgumentCount();

    protected void validateArgumentCountMatch(int parameterCount) {
        ExceptionUtility.throwExceptionIf(parameterCount != getExpectedArgumentCount(),
                ExecutionError.INVALID_ARGUMENT_COUNT_EXPECTED_ACTUAL,
                getName(), getExpectedArgumentCount(), parameterCount);
    }

    protected void validateArgumentNonNull(Object data, int argument) {
        ExceptionUtility.throwExceptionIf(Objects.isNull(data),
                ExecutionError.INVALID_TYPE_ARGUMENT_NON_NULL,
                getName(), argument);
    }

    protected void validateArgumentNonNull(Object data, String errorMessage) {
        ExceptionUtility.throwExceptionIf(Objects.isNull(data),
                ExecutionError.INVALID_TYPE_ARGUMENT_NON_NULL_CUSTOM_MESSAGE,
                errorMessage);
    }

    protected void validateArgumentDataType(int argument, Data data, Function<Data, Boolean> validatesDataType, String expectedDataType) {
        var dataType = Objects.isNull(data) ? null : data.dataType();
        ExceptionUtility.throwExceptionIf(Objects.isNull(data) || !validatesDataType.apply(data),
                ExecutionError.INVALID_TYPE_ARGUMENT_EXPECTED_ACTUAL,
                getName(), expectedDataType, dataType, argument);
    }

}
