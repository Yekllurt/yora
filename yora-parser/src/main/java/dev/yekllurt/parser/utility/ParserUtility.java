package dev.yekllurt.parser.utility;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.throwable.ParseException;

/**
 * A helper class containing functions that are used by the parser and interpreter
 */
public class ParserUtility {

    private ParserUtility() {
        // Intentional as a helper class should not be instantiated by other classes
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
                Double.valueOf(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
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

    public static boolean isNumber(Object value) {
        return isLong(value) || isDouble(value);
    }

    public static DataType getReturnType(Object value) {
        if (ParserUtility.isLong(value)) {
            return DataType.INT;
        }
        if (ParserUtility.isDouble(value)) {
            return DataType.FLOAT;
        }
        if (value instanceof Boolean) {
            return DataType.BOOLEAN;
        }
        if (value instanceof Character) {
            return DataType.STRING;
        }
        return null;
    }

}
