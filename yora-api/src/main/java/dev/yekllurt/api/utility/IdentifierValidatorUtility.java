package dev.yekllurt.api.utility;

import dev.yekllurt.api.errors.SemanticError;

import java.util.Objects;

public class IdentifierValidatorUtility {

    public enum IdentifierType {
        USER_VARIABLE,
        SYSTEM_VARIABLE,
        UNKNOWN // This is an error type, used for instance if the identifier is null
    }

    private IdentifierValidatorUtility() {
        // Intentional: other classes should not instantiate a helper class
    }

    public static IdentifierType getIdentifierType(String identifier) {
        if (Objects.isNull(identifier)) {
            return IdentifierType.UNKNOWN;
        }
        // Only system variables are allowed to be all upper case
        if (identifier.equals(identifier.toUpperCase())) {
            return IdentifierType.SYSTEM_VARIABLE;
        }
        return IdentifierType.USER_VARIABLE;
    }

    public static void performValidationIsValidUserVariableWhenDeclaring(int tokenCursor, String identifier) {
        switch (IdentifierValidatorUtility.getIdentifierType(identifier)) {
            case USER_VARIABLE -> {
                // Deliberately perform no action as it is a valid identifier
            }
            case SYSTEM_VARIABLE ->
                    ExceptionUtility.throwException(SemanticError.INVALID_VARIABLE_DECLARATION_NOT_FOLLOWING_GUIDELINES_CLASHES_WITH_SYSTEM_VARIABLE,
                            tokenCursor, identifier);
            default -> ExceptionUtility.throwException(SemanticError.INVALID_VARIABLE_NOT_FOLLOWING_GUIDELINES,
                    tokenCursor, identifier);
        }
    }

    public static void performValidationIsValidUserVariableWhenAssigning(int tokenCursor, String identifier) {
        switch (IdentifierValidatorUtility.getIdentifierType(identifier)) {
            case USER_VARIABLE -> {
                // Deliberately perform no action as it is a valid identifier
            }
            case SYSTEM_VARIABLE ->
                    ExceptionUtility.throwException(SemanticError.INVALID_VARIABLE_ASSIGNMENT_CANT_OVERRIDE_SYSTEM_VARIABLES,
                            tokenCursor, identifier);
            default -> ExceptionUtility.throwException(SemanticError.INVALID_VARIABLE_NOT_FOLLOWING_GUIDELINES,
                    tokenCursor, identifier);
        }
    }

}
