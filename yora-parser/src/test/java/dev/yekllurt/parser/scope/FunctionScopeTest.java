package dev.yekllurt.parser.scope;

import dev.yekllurt.api.throwable.execution.ScopeException;
import dev.yekllurt.parser.ast.impl.FunctionNode;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.impl.FunctionScopeImplementation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class FunctionScopeTest {

    private FunctionScope functionScope;

    @BeforeEach
    void beforeEach() {
        this.functionScope = new FunctionScopeImplementation();
    }

    @AfterEach
    void afterEach() {
        this.functionScope = null;
    }

    @Test
    void givenScopeWithoutHardScope_whenBeginSoftScope_thenThrowException() {
        assertThrows(ScopeException.class, () -> this.functionScope.beginSoftScope());
    }

    @Test
    void givenScopeWithoutHardScope_whenEndSoftScope_thenThrowException() {
        assertThrows(ScopeException.class, () -> this.functionScope.endSoftScope());
    }

    @Test
    void givenScopeWithoutSoftScope_whenEndSoftScope_thenThrowException() {
        this.functionScope.beginHardScope();
        assertThrows(ScopeException.class, () -> this.functionScope.endSoftScope());
        this.functionScope.endHardScope();
    }

    @Test
    void givenScope_whenStartAndEndHardScope_thenThrowNoException() {
        assertDoesNotThrow(() -> this.functionScope.beginHardScope());
        assertDoesNotThrow(() -> this.functionScope.endHardScope());
    }

    @Test
    void givenScopeWithHardScope_whenStartAndEndSoftScope_thenThrowNoException() {
        this.functionScope.beginHardScope();
        assertDoesNotThrow(() -> this.functionScope.beginSoftScope());
        assertDoesNotThrow(() -> this.functionScope.endSoftScope());
        this.functionScope.endHardScope();
    }

    @Test
    void givenScopeWithHardScope_whenStartAndEndMultipleSoftScope_thenThrowNoException() {
        this.functionScope.beginHardScope();
        assertDoesNotThrow(() -> this.functionScope.beginSoftScope());
        assertDoesNotThrow(() -> this.functionScope.endSoftScope());
        assertDoesNotThrow(() -> this.functionScope.beginSoftScope());
        assertDoesNotThrow(() -> this.functionScope.beginSoftScope());
        assertDoesNotThrow(() -> this.functionScope.beginSoftScope());
        assertDoesNotThrow(() -> this.functionScope.endSoftScope());
        assertDoesNotThrow(() -> this.functionScope.endSoftScope());
        assertDoesNotThrow(() -> this.functionScope.endSoftScope());
        this.functionScope.endHardScope();
    }

    @Test
    void givenScopeWithoutScope_whenAssignFunction_thenThrowException() {
        var functionNode = mockFunctionNode();
        assertThrows(ScopeException.class, () -> this.functionScope.assignFunction("test", functionNode));
    }

    @Test
    void givenScopeWithScope_whenAssignFunction_thenSuccess() {
        this.functionScope.beginHardScope();
        this.functionScope.beginSoftScope();
        var functionNode = mockFunctionNode();
        assertDoesNotThrow(() -> this.functionScope.assignFunction("test", functionNode));
        this.functionScope.endSoftScope();
        this.functionScope.endHardScope();
    }

    @Test
    void givenScopeWithFunctionTest_whenAssignFunctionTest_thenThrowException() {
        this.functionScope.beginHardScope();
        this.functionScope.beginSoftScope();
        var functionNode = mockFunctionNode();
        assertDoesNotThrow(() -> this.functionScope.assignFunction("test", functionNode));
        assertThrows(ScopeException.class, () -> this.functionScope.assignFunction("test", functionNode));
        this.functionScope.endSoftScope();
        this.functionScope.endHardScope();
    }

    @Test
    void givenScopeWithoutScope_whenLookupFunction_thenThrowException() {
        assertThrows(ScopeException.class, () -> this.functionScope.lookupFunction("test"));
    }

    @Test
    void givenScopeWithScope_whenLookupNonExistingFunction_thenThrowException() {
        this.functionScope.beginHardScope();
        this.functionScope.beginSoftScope();
        assertThrows(ScopeException.class, () -> this.functionScope.lookupFunction("test"));
        this.functionScope.endSoftScope();
        this.functionScope.endHardScope();
    }

    @Test
    void givenScopeWithScopeAndFunction_whenLookup_thenSuccess() {
        this.functionScope.beginHardScope();
        this.functionScope.beginSoftScope();
        var functionNode = mockFunctionNode();
        assertDoesNotThrow(() -> this.functionScope.assignFunction("test", functionNode));
        assertDoesNotThrow(() -> this.functionScope.lookupFunction("test"));
        var result = this.functionScope.lookupFunction("test");
        assertEquals(functionNode, result);
        this.functionScope.endSoftScope();
        this.functionScope.endHardScope();
    }

    private FunctionNode mockFunctionNode() {
        return Mockito.mock(FunctionNode.class);
    }

}
