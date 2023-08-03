package dev.yekllurt.parser.interpreter.scope;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.throwable.InvalidOperationError;

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
        if (!DataType.INT.equals(dataType)) {
            throw new InvalidOperationError(String.format("Can't convert data from the type %s into an int", dataType));
        }
        if (data instanceof Integer i) {
            System.err.println("WARNING: using an integer in the code, this should not happen");
            return i.longValue();
        }
        return (Long) data;

    }

    public Double toDouble() {
        if (!(DataType.FLOAT.equals(dataType) || DataType.INT.equals(dataType))) {
            throw new InvalidOperationError(String.format("Can't convert data from the type %s into a double", dataType));
        }
        if (DataType.INT.equals(dataType)) {
            if (data instanceof Integer i) {
                System.err.println("WARNING: using an integer in the code, this should not happen");
                return i.doubleValue();
            }
            return ((Long) data).doubleValue();
        }
        return (Double) data;
    }

    public boolean toBoolean() {
        if (!DataType.BOOLEAN.equals(dataType)) {
            throw new InvalidOperationError(String.format("Can't convert data from the type %s into an int", dataType));
        }
        return (boolean) data;
    }

    public String[] toStringArray() {
        if (!DataType.STRING_ARRAY.equals(dataType)) {
            throw new InvalidOperationError(String.format("Can't convert data from the type %s into a string array", dataType));
        }
        return (String[]) data;
    }

    public Long[] toLongArray() {
        if (!DataType.INT_ARRAY.equals(dataType)) {
            throw new InvalidOperationError(String.format("Can't convert data from the type %s into an int array", dataType));
        }
        return (Long[]) data;
    }

    public Double[] toDoubleArray() {
        if (!DataType.FLOAT_ARRAY.equals(dataType)) {
            throw new InvalidOperationError(String.format("Can't convert data from the type %s into a float array", dataType));
        }
        return (Double[]) data;
    }

    public boolean[] toBooleanArray() {
        if (!DataType.BOOLEAN_ARRAY.equals(dataType)) {
            throw new InvalidOperationError(String.format("Can't convert data from the type %s into a float array", dataType));
        }
        return (boolean[]) data;
    }

    public String toString() {
        return String.valueOf(data);
    }

}