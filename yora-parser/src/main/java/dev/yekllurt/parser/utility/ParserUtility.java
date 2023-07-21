package dev.yekllurt.parser.utility;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.throwable.ParseException;
import dev.yekllurt.parser.interpreter.throwable.ExecutionError;

/**
 * A helper class containing functions that are used by the parser and interpreter
 */
public class ParserUtility {

    private ParserUtility() {
        // Intentional: other classes should not instantiate a helper class
    }

    public static boolean isBoolean(Object value) {
        return value instanceof Boolean;
    }

    public static boolean isString(Object value) {
        if (value instanceof String str) {
            return str.matches(".*?");
        }
        return false;
    }

    public static boolean isIdentifier(Object value) {
        if (value instanceof String str) {
            return str.matches("[a-zA-Z]+");
        }
        return false;
    }

    public static boolean isInt(Object value) {
        if (value instanceof Integer) {
            return true;
        }
        if (value instanceof String str) {
            try {
                Integer.valueOf(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public static Integer parseInt(Object value) {
        if (!isInt(value)) {
            throw new ParseException(String.format("Can't convert the value '%s' into an integer", value));
        }
        if (value instanceof Integer i) {
            return i;
        } else if (value instanceof String str) {
            return Integer.valueOf(str);
        }
        throw new ParseException(String.format("Can't convert the value '%s' into an integer", value));
    }

    public static boolean isLong(Object value) {
        if (value instanceof Integer) {
            return true;
        }
        if (value instanceof Long) {
            return true;
        }
        if (value instanceof String str) {
            try {
                Long.valueOf(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public static Long parseLong(Object value) {
        if (!isLong(value)) {
            throw new ParseException(String.format("Can't convert the value '%s' into an integer", value));
        }
        if (value instanceof Long l) {
            return l;
        } else if (value instanceof Integer i) {
            return i.longValue();
        } else if (value instanceof String str) {
            return Long.valueOf(str);
        }
        throw new ParseException(String.format("Can't convert the value '%s' into an integer", value));
    }

    public static boolean isDouble(Object value) {
        if (value instanceof Float) {
            return true;
        }
        if (value instanceof Double) {
            return true;
        }
        if (value instanceof String str) {
            try {
                Float.valueOf(str);
                return true;
            } catch (NumberFormatException e) {
                // Intentionally do nothing
            }
            try {
                Double.valueOf(str);
                return true;
            } catch (NumberFormatException e) {
                // Intentionally do nothing
            }
        }
        return false;
    }

    public static Double parseDouble(Object value) {
        if (!isNumber(value)) {
            throw new ParseException(String.format("Can't convert the value '%s' into a float", value));
        }
        if (value instanceof Double d) {
            return d;
        } else if (value instanceof Float f) {
            return f.doubleValue();
        } else if (value instanceof Integer i) {
            return i.doubleValue();
        } else if (value instanceof Long l) {
            return l.doubleValue();
        } else if (value instanceof String str) {
            return Double.valueOf(str);
        }
        throw new ParseException(String.format("Can't convert the value '%s' into a float", value));
    }

    private static boolean isNumber(Object value) {
        return isLong(value) || isDouble(value);
    }

    public static boolean isNumber(DataType dataType) {
        return dataType.equals(DataType.INT) || dataType.equals(DataType.FLOAT);
    }

    public static boolean isStringArray(Object value) {
        return value instanceof String[];
    }

    public static boolean isLongArray(Object value) {
        return value instanceof long[] || value instanceof Long[];
    }

    public static boolean isDoubleArray(Object value) {
        return value instanceof double[] || value instanceof Double[];
    }

    public static boolean isBooleanArray(Object value) {
        return value instanceof boolean[] || value instanceof Boolean[];
    }

    public static boolean isArray(DataType dataType) {
        return DataType.STRING_ARRAY == dataType || DataType.INT_ARRAY == dataType || DataType.FLOAT_ARRAY == dataType || DataType.BOOLEAN_ARRAY == dataType;
    }

    public static DataType getReturnType(Object value) {
        if (isLong(value)) {
            return DataType.INT;
        }
        if (isDouble(value)) {
            return DataType.FLOAT;
        }
        if (isString(value)) {
            return DataType.STRING;
        }
        if (isBoolean(value)) {
            return DataType.BOOLEAN;
        }
        if (isLongArray(value)) {
            return DataType.INT_ARRAY;
        }
        if (isDoubleArray(value)) {
            return DataType.FLOAT_ARRAY;
        }
        if (isStringArray(value)) {
            return DataType.STRING_ARRAY;
        }
        if (isBooleanArray(value)) {
            return DataType.BOOLEAN_ARRAY;
        }
        throw new ExecutionError(String.format("Unable to get the data type for the value '%s'", value));
    }

}
