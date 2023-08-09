package dev.yekllurt.api;

import dev.yekllurt.api.errors.ExecutionError;
import dev.yekllurt.api.utility.ExceptionUtility;

import java.util.Arrays;

public enum DataType {

    INT("INT"),
    FLOAT("FLOAT"),
    BOOLEAN("BOOL"),
    STRING("STR"),
    INT_ARRAY("INT_ARRAY"),
    FLOAT_ARRAY("FLOAT_ARRAY"),
    BOOLEAN_ARRAY("BOOL_ARRAY"),
    STRING_ARRAY("STR_ARRAY");

    private final String friendlyName;

    DataType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public static DataType fromString(String dataType, boolean isArray) {
        if (isArray) {
            dataType = dataType + "_ARRAY";
        }
        for (var value : values()) {
            if (value.getFriendlyName().equals(dataType)) {
                return value;
            }
        }
        return null;
    }

    public static Object createArray(DataType dataType, int size) {
        switch (dataType) {
            case INT_ARRAY -> {
                var arr = new Long[size];
                Arrays.fill(arr, 0L);   // Default value of 0
                return arr;
            }
            case FLOAT_ARRAY -> {
                var arr = new Double[size];
                Arrays.fill(arr, 0D);   // Default value of 0
                return arr;
            }
            case STRING_ARRAY -> {
                var arr = new String[size];
                Arrays.fill(arr, "");   // Default value of ""
                return arr;
            }
            case BOOLEAN_ARRAY -> {
                var arr = new boolean[size];
                Arrays.fill(arr, false);    // Default value of false
                return arr;
            }
            default -> ExceptionUtility.throwException(ExecutionError.INVALID_CONVERSION_INTO_ARRAY, dataType);
        }
        return null; // Should never reach this code part
    }

    public String getFriendlyName() {
        return friendlyName;
    }

}
