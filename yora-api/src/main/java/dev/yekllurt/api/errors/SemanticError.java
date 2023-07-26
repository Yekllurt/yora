package dev.yekllurt.api.errors;

import dev.yekllurt.api.throwable.compilation.SemanticException;

public class SemanticError {

    // === Method errors ===
    public static final Error INVALID_METHOD_DEFINITION_TOO_MANY_PARAMETERS = new Error(-1, SemanticException.class,
            "Invalid method definition for the method '%s' as '%s' parameters are provided but only '%s' are allowed");

    public static final Error INVALID_METHOD_EXPECTED_RETURN_STATEMENT = new Error(-1, SemanticException.class,
            "Invalid method definition for the method '%s' as a return statement is expected but non is provided");

    public static final Error INVALID_METHOD_EXPECTED_NO_RETURN_STATEMENT = new Error(-1, SemanticException.class,
            "Invalid method definition for the method '%s' as no return statement is expected but one is provided");

    // === Variable errors ===
    public static final Error INVALID_VARIABLE_NOT_FOLLOWING_GUIDELINES = new Error(-1, SemanticException.class,
            "Invalid variable at token %s. The identifier '%s' doesn't follow the variable naming convention");

    public static final Error INVALID_VARIABLE_DECLARATION_NOT_FOLLOWING_GUIDELINES_CLASHES_WITH_SYSTEM_VARIABLE = new Error(-1, SemanticException.class,
            "Invalid variable at token %s. The identifier '%s' doesn't follow the variable naming convention => it clashes with the system variable naming convention");

    public static final Error INVALID_VARIABLE_ASSIGNMENT_CANT_OVERRIDE_SYSTEM_VARIABLES = new Error(-1, SemanticException.class,
            "Invalid variable assignment at token %s. Can't override system variables.");

    private SemanticError() {
        // Intentional: other classes should not instantiate a helper class
    }

}
