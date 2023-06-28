package dev.yekllurt.api;

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

    public String getName() {
        return name;
    }

}
