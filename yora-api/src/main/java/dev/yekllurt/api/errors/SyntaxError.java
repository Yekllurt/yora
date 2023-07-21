package dev.yekllurt.api.errors;

import dev.yekllurt.api.throwable.SyntaxException;

public class SyntaxError {

    // === General errors ===
    public static final Error INVALID_PROGRAM = new Error(-1, SyntaxException.class,
            "Invalid program. Only %s out of %s token(s) have been used",
            "The program is not valid as not all the provided tokens are used");

    public static final Error INVALID_STATEMENT_NULL = new Error(-1, SyntaxException.class,
            "Invalid statement at token %s. Expected a statement but didn't find one.",
            "");

    public static final Error INVALID_CONDITION_NULL = new Error(-1, SyntaxException.class,
            "Invalid condition at token %s. Expected a condition but didn't find one.",
            "");

    // === Method errors ===
    public static final Error INVALID_METHOD_CALL = new Error(-1, SyntaxException.class,
            "Invalid function call at token %s for function '%s'",
            "");

    public static final Error INVALID_METHOD_NO_IDENTIFIER = new Error(-1, SyntaxException.class,
            "Invalid method at token %s as no identifier is provided",
            "");

    public static final Error INVALID_METHOD_ARGUMENT_LIST_MISSING_ARGUMENT_AFTER_COMMA = new Error(-1, SyntaxException.class,
            "Invalid method parameter at token %s. Expected a parameter after a ',' however non was found",
            "");
    public static final Error INVALID_METHOD_EXPECTED_ACTUAL = new Error(-1, SyntaxException.class,
            "Invalid method at token %s. Expected '%s' but found '%s'",
            "");

    public static final Error INVALID_METHOD_EXPECTED_ONE_OF_ACTUAL = new Error(-1, SyntaxException.class,
            "Invalid method at token %s. Expected one of the following '%s' but found '%s' with value '%s'",
            "");

    // === Argument errors ===
    public static final Error INVALID_ARGUMENT_NO_IDENTIFIER = new Error(-1, SyntaxException.class,
            "Invalid argument at token %s as no identifier is provided",
            "");

    public static final Error INVALID_ARGUMENT_EXPECTED_ACTUAL = new Error(-1, SyntaxException.class,
            "Invalid argument at token %s. Expected '%s' but found '%s'",
            "");

    public static final Error INVALID_ARGUMENT_EXPECTED_ONE_OF_ACTUAL = new Error(-1, SyntaxException.class,
            "Invalid argument at token %s. Expected one of the following '%s' but found '%s' with value '%s'",
            "");

    // === Variable errors ===

    // === Statement errors ===
    public static final Error INVALID_STATEMENT_ATTEMPTING_TO_DECLARE_ARRAY_BUT_FOUND_NO_SIZE = new Error(-1, SyntaxException.class,
            "Invalid statement at token %s. Expected to find an integer which defines the array size when declaring a variable but found instead '%s'",
            "");

    public static final Error INVALID_STATEMENT_ATTEMPTING_TO_DECLARE_VARIABLE_BUT_FOUND_NO_IDENTIFIER = new Error(-1, SyntaxException.class,
            "Invalid statement at token %s. Expected to find an identifier when declaring a variable but found instead '%s'.",
            "");

    public static final Error INVALID_STATEMENT_EXPECTED_EXPRESSION_BUT_FOUND_NONE = new Error(-1, SyntaxException.class,
            "Invalid statement at token %s. Expected to find an expression but found none",
            "");

    public static final Error INVALID_STATEMENT_EXPECTED_ACTUAL = new Error(-1, SyntaxException.class,
            "Invalid statement at token %s. Expected '%s' but found '%s'",
            "");

    // === Condition errors ===
    public static final Error INVALID_CONDITION_UNSUPPORTED_OPERATOR = new Error(-1, SyntaxException.class,
            "Invalid condition at token %s. The operator '%s' is not supported",
            "");

    public static final Error INVALID_CONDITION_EXPECTED_ACTUAL = new Error(-1, SyntaxException.class,
            "Invalid condition at token %s. Expected '%s' but found '%s'",
            "");

    // === Expression errors ===
    public static final Error INVALID_EXPRESSION_LIST_MISSING_EXPRESSION_AFTER_COMMA = new Error(-1, SyntaxException.class,
            "Invalid expression at token %s. Expected an expression after a ',' however non was found",
            "");

    // === ===

    public static final Error INVALID_ARRAY_INDEX_EXPRESSION = new Error(-1, SyntaxException.class,
            "Invalid array index expression at token %s",
            "");

    public static final Error INVALID_ARRAY_READ_VALUE_CALL = new Error(-1, SyntaxException.class,
            "Invalid array read value call",
            "Attempting to read a value from an array using an invalid syntax");

    public static final Error INVALID_ATOM = new Error(-1, SyntaxException.class,
            "Invalid atom at token %s.",
            "");

    public static final Error INVALID_ATOM_NO_EXPRESSION = new Error(-1, SyntaxException.class,
            "Invalid atom at token %s.",
            "");

    public static final Error INVALID_ATOM_EXPECTED_ACTUAL = new Error(-1, SyntaxException.class,
            "Invalid atom at token %s. Expected '%s' but found '%s'",
            "");

    public static final Error INVALID_ATOM_UNSUPPORTED_TOKEN_TYPE = new Error(-1, SyntaxException.class,
            "Invalid atom at token %s. The token '%s' is not supported in that context",
            "");

    private SyntaxError() {
        // Intentional: other classes should not instantiate a helper class
    }

}
