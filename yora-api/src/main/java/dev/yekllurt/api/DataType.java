package dev.yekllurt.api;

import java.util.Arrays;

public enum DataType {

    INT("INT"),
    FLOAT("FLOAT"),
    BOOLEAN("BOOLEAN"),
    STRING("STRING"),
    INT_ARRAY("INT_ARRAY"),
    FLOAT_ARRAY("FLOAT_ARRAY"),
    BOOLEAN_ARRAY("BOOLEAN_ARRAY"),
    STRING_ARRAY("STRING_ARRAY");

    private final String name;

    DataType(String name) {
        this.name = name;
    }

    public static DataType fromString(String dataType, boolean isArray) {
        if (isArray) {
            dataType = dataType + "_ARRAY";
        }
        for (var value : values()) {
            if (value.getName().equals(dataType)) {
                return value;
            }
        }
        return null;
    }

    public static Object createArray(DataType dataType, int size) {
        switch (dataType) {
            case INT_ARRAY -> {
                var arr = new Long[size];
                Arrays.fill(arr, 0L);
                return arr;
            }
            case FLOAT_ARRAY -> {
                var arr = new Double[size];
                Arrays.fill(arr, 0D);
                return arr;
            }
            case STRING_ARRAY -> {
                return new String[size]; // TODO: should the array have empty strings as default value instead of null?
            }
            default ->
                    throw new UnsupportedOperationException(String.format("The datatype %s can't be converted into an array", size));
        }
    }

    public String getName() {
        return name;
    }

}
