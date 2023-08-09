package dev.yekllurt.api.errors;

import dev.yekllurt.api.throwable.compilation.LexicalException;

/**
 * Error code meaning: <br>
 * - 11xxx the error occurs in the token scanning <br>
 * - 12xxx the error occurs in the token classification
 */
public class LexicalError {

    public static final Error INVALID_TOKEN_DEFINITION_LINE = new Error(11000, LexicalException.class,
            "Invalid token definition in line %s of the file '%s'");

    public static final Error INVALID_CHARACTER = new Error(11001, LexicalException.class,
            "Invalid character '%s' at position %s");

    public static final Error UNKNOWN_TOKEN_CLASSIFIER_FILE_SECTION = new Error(12000, LexicalException.class,
            "Unknown file section in the file '%s'");

    public static final Error REFERENCING_UNKNOWN_PATTERN_DEFINITION = new Error(12001, LexicalException.class,
            "The referenced pattern '%s' is not defined");

    public static final Error UNKNOWN_PATTERN_DEFINITION = new Error(12002, LexicalException.class,
            "Unknown pattern definitions '%s' for a token definition");

    public static final Error INVALID_TOKEN = new Error(12003, LexicalException.class,
            "Invalid token '%s'");

}
