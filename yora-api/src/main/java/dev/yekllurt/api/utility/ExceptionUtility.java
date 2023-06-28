package dev.yekllurt.api.utility;

public class ExceptionUtility {

    private ExceptionUtility() {
        // Intentional as a helper class should not be instantiated by other classes
    }

    public static void throwIf(boolean condition, RuntimeException exception) {
        if (condition) {
            throw exception;
        }
    }

}
