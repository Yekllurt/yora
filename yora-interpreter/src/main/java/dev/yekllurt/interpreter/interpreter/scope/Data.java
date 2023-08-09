package dev.yekllurt.interpreter.interpreter.scope;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.errors.ExecutionError;
import dev.yekllurt.api.utility.ExceptionUtility;

public record Data(DataType dataType, Object data) {

    public boolean isString() {
        return DataType.STRING.equals(dataType);
    }

    public boolean isLong() {
        return DataType.INT.equals(dataType);
    }

    public boolean isDouble() {
        return DataType.FLOAT.equals(dataType);
    }

    public boolean isNumber() {
        return isLong() || isDouble();
    }

    public boolean isBoolean() {
        return DataType.BOOLEAN.equals(dataType);
    }

    public boolean isStringArray() {
        return DataType.STRING_ARRAY.equals(dataType);
    }

    public boolean isLongArray() {
        return DataType.INT_ARRAY.equals(dataType);
    }

    public boolean isDoubleArray() {
        return DataType.FLOAT_ARRAY.equals(dataType);
    }

    public boolean isBooleanArray() {
        return DataType.BOOLEAN_ARRAY.equals(dataType);
    }

    public boolean isArray() {
        return isStringArray() || isLongArray() || isDoubleArray() || isBooleanArray();
    }

    public Long toLong() {
        ExceptionUtility.throwExceptionIf(!DataType.INT.equals(dataType),
                ExecutionError.INVALID_CONVERSION_TYPES,
                dataType, "int");
        return (Long) data;

    }

    public Double toDouble() {
        ExceptionUtility.throwExceptionIf(!(DataType.FLOAT.equals(dataType) || DataType.INT.equals(dataType)),
                ExecutionError.INVALID_CONVERSION_TYPES,
                dataType, "float");
        if (DataType.INT.equals(dataType)) {
            return ((Long) data).doubleValue();
        }
        return (Double) data;
    }

    public boolean toBoolean() {
        ExceptionUtility.throwExceptionIf(!DataType.BOOLEAN.equals(dataType),
                ExecutionError.INVALID_CONVERSION_TYPES,
                dataType, "boolean");
        return (boolean) data;
    }

    public String[] toStringArray() {
        ExceptionUtility.throwExceptionIf(!DataType.STRING_ARRAY.equals(dataType),
                ExecutionError.INVALID_CONVERSION_TYPES,
                dataType, "string array");
        return (String[]) data;
    }

    public Long[] toLongArray() {
        ExceptionUtility.throwExceptionIf(!DataType.INT_ARRAY.equals(dataType),
                ExecutionError.INVALID_CONVERSION_TYPES,
                dataType, "int array");
        return (Long[]) data;
    }

    public Double[] toDoubleArray() {
        ExceptionUtility.throwExceptionIf(!DataType.FLOAT_ARRAY.equals(dataType),
                ExecutionError.INVALID_CONVERSION_TYPES,
                dataType, "float array");
        return (Double[]) data;
    }

    public boolean[] toBooleanArray() {
        ExceptionUtility.throwExceptionIf(!DataType.BOOLEAN_ARRAY.equals(dataType),
                ExecutionError.INVALID_CONVERSION_TYPES,
                dataType, "boolean array");
        return (boolean[]) data;
    }

    public String toString() {
        return String.valueOf(data);
    }

}
