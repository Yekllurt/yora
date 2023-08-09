package dev.yekllurt.api.errors;

import dev.yekllurt.api.throwable.execution.ScopeException;

/**
 * Error code meaning: <br>
 * - 40xxx scope management errors <br>
 * - 41xxx scope modification errors <br>
 * - 42xxx scope fetching errors <br>
 * - 43xxx scope other errors <br>
 */
public class ScopeError {

    // === Scope management ===
    public static final Error CAN_NOT_BEGIN_SOFT_SCOPE = new Error(40000, ScopeException.class,
            "Can't create a %s soft scope as no hard scope is active.");
    public static final Error CAN_NOT_END_SOFT_SCOPE = new Error(40001, ScopeException.class,
            "Can't end a %s soft scope as no hard scope is active.");
    public static final Error CAN_NOT_END_HARD_SCOPE = new Error(40002, ScopeException.class,
            "Can't end a %s hard scope as no scope is active.");

    // === Scope modification ===
    public static final Error CAN_NOT_ASSIGN_DATA_BECAUSE_NO_ACTIVE_SOFT_SCOPE = new Error(41000, ScopeException.class,
            "Can't assign the %s '%s' as there is no active soft scope available.");

    public static final Error CAN_NOT_ASSIGN_DATA_BECAUSE_IT_ALREADY_EXISTS_IN_SCOPE = new Error(41001, ScopeException.class,
            "Can't assign the %s '%s' as it already exists in the current soft scope.");

    public static final Error CAN_NOT_ASSIGN_DATA_BECAUSE_IT_ALREADY_WAS_ASSIGNED = new Error(41002, ScopeException.class,
            "Can't assign the %s value as in the scope the value has already been assigned.");

    public static final Error CAN_NOT_UPDATE_DATA_BECAUSE_NO_ACTIVE_SOFT_SCOPE = new Error(41003, ScopeException.class,
            "Can't update the %s '%s' as there is not active soft scope available.");

    public static final Error CAN_NOT_UPDATE_DATA_BECAUSE_IT_DOES_NOT_EXISTS_IN_SCOPE = new Error(41004, ScopeException.class,
            "Can't update the %s '%s' as it doesn't exist in the current soft scope.");

    // === Scope fetching ===
    public static final Error CAN_NOT_CHECK_EXISTENCE_BECAUSE_NO_ACTIVE_SCOPE = new Error(42000, ScopeException.class,
            "Can't check if the %s '%s' exists as there is no active soft scope available.");

    public static final Error CAN_NOT_LOOKUP_DATA_BECAUSE_NO_ACTIVE_SCOPE = new Error(42001, ScopeException.class,
            "Can't lookup the %s '%s' as there is no active soft scope available.");

    public static final Error CAN_NOT_FIND_DATA_IN_SCOPE = new Error(42002, ScopeException.class,
            "Can't find the %s '%s' in the current scope or any parent soft scope.");

    // === Scope other ===
    public static final Error RETURN_SCOPE_IS_NULL = new Error(43000, ScopeException.class,
            "Unable to return a value as the return scope is null");

    private ScopeError() {
        // Intentional: other classes should not instantiate a helper class
    }

}
