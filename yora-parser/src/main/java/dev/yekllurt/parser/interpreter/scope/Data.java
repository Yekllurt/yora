package dev.yekllurt.parser.interpreter.scope;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.interpreter.throwable.InvalidOperationError;

public record Data(DataType dataType, Object data) {

    public boolean isLong() {
        return DataType.INT.equals(dataType);
    }

    public boolean isDouble() {
        return DataType.FLOAT.equals(dataType);
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

    public Boolean toBoolean() {
        if (!DataType.BOOLEAN.equals(dataType)) {
            throw new InvalidOperationError(String.format("Can't convert data from the type %s into an int", dataType));
        }
        return (Boolean) data;
    }

    public String toString() {
        return String.valueOf(data);
    }

}
