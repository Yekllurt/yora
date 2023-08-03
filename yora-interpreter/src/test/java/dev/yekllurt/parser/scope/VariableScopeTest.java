package dev.yekllurt.parser.scope;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.throwable.execution.ScopeException;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.VariableScopeImplementation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariableScopeTest {

    private VariableScope variableScope;

    @BeforeEach
    void beforeEach() {
        this.variableScope = new VariableScopeImplementation();
    }

    @AfterEach
    void afterEach() {
        this.variableScope = null;
    }

    @Test
    void givenScopeWithoutHardScope_whenBeginSoftScope_thenThrowException() {
        assertThrows(ScopeException.class, () -> this.variableScope.beginSoftScope());
    }

    @Test
    void givenScopeWithoutHardScope_whenEndSoftScope_thenThrowException() {
        assertThrows(ScopeException.class, () -> this.variableScope.endSoftScope());
    }

    @Test
    void givenScopeWithoutSoftScope_whenEndSoftScope_thenThrowException() {
        this.variableScope.beginHardScope();
        assertThrows(ScopeException.class, () -> this.variableScope.endSoftScope());
        this.variableScope.endHardScope();
    }

    @Test
    void givenScope_whenStartAndEndHardScope_thenThrowNoException() {
        assertDoesNotThrow(() -> this.variableScope.beginHardScope());
        assertDoesNotThrow(() -> this.variableScope.endHardScope());
    }

    @Test
    void givenScopeWithHardScope_whenStartAndEndSoftScope_thenThrowNoException() {
        this.variableScope.beginHardScope();
        assertDoesNotThrow(() -> this.variableScope.beginSoftScope());
        assertDoesNotThrow(() -> this.variableScope.endSoftScope());
        this.variableScope.endHardScope();
    }

    @Test
    void givenScopeWithHardScope_whenStartAndEndMultipleSoftScope_thenThrowNoException() {
        this.variableScope.beginHardScope();
        assertDoesNotThrow(() -> this.variableScope.beginSoftScope());
        assertDoesNotThrow(() -> this.variableScope.endSoftScope());
        assertDoesNotThrow(() -> this.variableScope.beginSoftScope());
        assertDoesNotThrow(() -> this.variableScope.beginSoftScope());
        assertDoesNotThrow(() -> this.variableScope.beginSoftScope());
        assertDoesNotThrow(() -> this.variableScope.endSoftScope());
        assertDoesNotThrow(() -> this.variableScope.endSoftScope());
        assertDoesNotThrow(() -> this.variableScope.endSoftScope());
        this.variableScope.endHardScope();
    }

    @Test
    void givenScopeWithHardAndSoftScope_whenAssignData_thenThrowNoException() {
        this.variableScope.beginHardScope();
        this.variableScope.beginSoftScope();
        assertDoesNotThrow(() -> this.variableScope.assignData("test", DataType.INT, 9L));
        this.variableScope.endSoftScope();
        this.variableScope.endHardScope();
    }

    @Test
    void givenScope_whenAssignSameDataInDifferentSoftScopes_thenThrowNoException() {
        this.variableScope.beginHardScope();
        this.variableScope.beginSoftScope();
        assertDoesNotThrow(() -> this.variableScope.assignData("test", DataType.INT, 9L));
        this.variableScope.endSoftScope();
        this.variableScope.beginSoftScope();
        assertDoesNotThrow(() -> this.variableScope.assignData("test", DataType.INT, 9L));
        this.variableScope.endSoftScope();
        this.variableScope.endHardScope();
    }

    @Test
    void givenScope_whenAssignSameDataInChildSoftScopes_thenThrowException() {
        this.variableScope.beginHardScope();
        this.variableScope.beginSoftScope();
        assertDoesNotThrow(() -> this.variableScope.assignData("test", DataType.INT, 9L));
        this.variableScope.beginSoftScope();
        assertThrows(ScopeException.class, () -> this.variableScope.assignData("test", DataType.INT, 9L));
        this.variableScope.endSoftScope();
        this.variableScope.endSoftScope();
        this.variableScope.endHardScope();
    }

    @Test
    void givenScope_whenUpdateNonPresentData_thenThrowException() {
        this.variableScope.beginHardScope();
        this.variableScope.beginSoftScope();
        assertThrows(ScopeException.class, () -> this.variableScope.updateData("test", 9L));
        this.variableScope.endSoftScope();
        this.variableScope.endHardScope();
    }

    @Test
    void givenScope_whenUpdatePresentData_thenThrowNoException() {
        this.variableScope.beginHardScope();
        this.variableScope.beginSoftScope();
        this.variableScope.assignData("test", DataType.INT, 9L);
        assertEquals(9L, this.variableScope.lookupData("test"));
        assertDoesNotThrow(() -> this.variableScope.updateData("test", 2L));
        assertEquals(2L, this.variableScope.lookupData("test"));
        this.variableScope.endSoftScope();
        this.variableScope.endHardScope();
    }

    @Test
    void givenScopeNoScope_whenLookup_thenThrowException() {
        assertThrows(ScopeException.class, () -> this.variableScope.lookup("test"));
        assertThrows(ScopeException.class, () -> this.variableScope.lookupData("test"));
        assertThrows(ScopeException.class, () -> this.variableScope.lookupDataType("test"));
    }

    @Test
    void givenScopeWithScope_whenLookupNonPresentData_thenThrowException() {
        this.variableScope.beginHardScope();
        this.variableScope.beginSoftScope();
        assertThrows(ScopeException.class, () -> this.variableScope.lookup("test"));
        assertThrows(ScopeException.class, () -> this.variableScope.lookupData("test"));
        assertThrows(ScopeException.class, () -> this.variableScope.lookupDataType("test"));
        this.variableScope.endSoftScope();
        this.variableScope.endHardScope();
    }

    @Test
    void givenScope_whenLookupData_thenThrowNoException() {
        this.variableScope.beginHardScope();
        this.variableScope.beginSoftScope();
        this.variableScope.assignData("test", DataType.INT, 9L);
        assertDoesNotThrow(() -> this.variableScope.lookup("test"));
        assertEquals(9L, this.variableScope.lookupData("test"));
        assertEquals(DataType.INT, this.variableScope.lookupDataType("test"));
        assertEquals(new Data(DataType.INT, 9L), this.variableScope.lookup("test"));
        this.variableScope.endSoftScope();
        this.variableScope.endHardScope();
    }

    @Test
    void givenScope_whenLookupDataInChildScope_thenThrowNoException() {
        this.variableScope.beginHardScope();
        this.variableScope.beginSoftScope();
        this.variableScope.assignData("test", null, null);
        this.variableScope.beginSoftScope();
        assertDoesNotThrow(() -> this.variableScope.lookup("test"));
        this.variableScope.endSoftScope();
        this.variableScope.endSoftScope();
        this.variableScope.endHardScope();
    }

    @Test
    void givenScope_whenLookupDataInParentScope_thenThrowException() {
        this.variableScope.beginHardScope();
        this.variableScope.beginSoftScope();
        this.variableScope.beginSoftScope();
        this.variableScope.assignData("test", DataType.INT, 9L);
        assertDoesNotThrow(() -> this.variableScope.lookup("test"));
        this.variableScope.endSoftScope();
        assertThrows(ScopeException.class, () -> this.variableScope.lookup("test"));
        this.variableScope.endSoftScope();
        this.variableScope.endHardScope();
    }

}
