package dev.yekllurt.parser.ast;

import dev.yekllurt.parser.ast.throwable.exception.ParseException;

/**
 * A helper class containing functions that are used by the parser and interpreter
 */
public class Utility {

    private Utility() {
        // Intentional as a helper class should not be instantiated by other classes
    }

    public static boolean isIdentifier(Object value) {
        if (value instanceof String str) {
            return str.matches("[a-zA-Z]+");
        }
        return false;
    }

    public static boolean isInteger(Object value) {
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

    public static Integer parseInteger(Object value) {
        if (!isInteger(value)) {
            throw new ParseException(String.format("Can't convert the value '%s' into an integer", value));
        }
        if (value instanceof Integer i) {
            return i;
        } else if (value instanceof String str) {
            return Integer.valueOf(str);
        }
        throw new ParseException(String.format("Can't convert the value '%s' into an integer", value));
    }

    public static boolean isFloat(Object value) {
        if (value instanceof Float) {
            return true;
        }
        if (value instanceof String str) {
            try {
                Float.valueOf(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public static Float parseFloat(Object value) {
        if (!isFloat(value)) {
            throw new ParseException(String.format("Can't convert the value '%s' into a float", value));
        }
        if (value instanceof Float f) {
            return f;
        } else if (value instanceof String str) {
            return Float.valueOf(str);
        }
        throw new ParseException(String.format("Can't convert the value '%s' into a float", value));
    }

    public static boolean isNumber(Object value) {
        return isInteger(value) || isFloat(value);
    }

}
