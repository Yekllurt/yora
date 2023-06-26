package dev.yekllurt.parser.utility;

public class ValidationUtility {

    private ValidationUtility() {
        // Intentional as a helper class should not be instantiated by other classes
    }

    public static void validate(boolean condition, RuntimeException exception) {
        if (!condition) {
            throw exception;
        }
    }

}
