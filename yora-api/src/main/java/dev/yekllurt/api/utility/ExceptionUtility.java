package dev.yekllurt.api.utility;

import dev.yekllurt.api.errors.Error;

import java.lang.reflect.InvocationTargetException;

public class ExceptionUtility {

    private ExceptionUtility() {
        // Intentional: other classes should not instantiate a helper class
    }

    public static void throwExceptionIf(boolean condition, RuntimeException exception) {
        if (condition) {
            throw exception;
        }
    }

    public static void throwException(Error error, Object... messageVariables) {
        try {
            var arg = String.format("[%s] %s", error.code(), String.format(error.message(), messageVariables));
            throw error.exception().getConstructor(String.class).newInstance(arg);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void throwExceptionIf(boolean condition, Error error, Object... messageVariables) {
        if (condition) {
            throwException(error, messageVariables);
        }
    }

}
