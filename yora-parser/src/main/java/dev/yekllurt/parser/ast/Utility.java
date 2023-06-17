package dev.yekllurt.parser.ast;

import dev.yekllurt.parser.ast.throwable.exception.ParseException;
import dev.yekllurt.parser.token.TokenType;

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

    public static boolean isLong(Object value) {
        if (value instanceof Integer) {
            return true;
        }
        if (value instanceof Long) {
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

    public static String getReturnType(Object value) {
        if (Utility.isLong(value)) {
            return TokenType.KEYWORD_INT;
        }
        if (Utility.isDouble(value)) {
            return TokenType.KEYWORD_FLOAT;
        }
        if (value instanceof Boolean) {
            return TokenType.KEYWORD_BOOLEAN;
        }
        if (value instanceof Character) {
            return TokenType.KEYWORD_CHAR;
        }
        return null;
    }

}
