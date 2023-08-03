package dev.yekllurt.parser.scope;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.throwable.execution.ScopeException;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.interpreter.scope.ParameterScope;
import dev.yekllurt.parser.interpreter.scope.impl.ParameterScopeImplementation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParameterScopeTest {

    private ParameterScope parameterScope;

    @BeforeEach
    void beforeEach() {
        this.parameterScope = new ParameterScopeImplementation();
    }

    @AfterEach
    void afterEach() {
        this.parameterScope = null;
    }

    @Test
    void givenParameterScope_whenNoParameterValue_thenThrowException() {
        assertThrows(ScopeException.class, () -> this.parameterScope.lookup("test"));
        assertThrows(ScopeException.class, () -> this.parameterScope.lookupData("test"));
        assertThrows(ScopeException.class, () -> this.parameterScope.lookupDataType("test"));
    }

    @Test
    void givenParameterScope_whenAddTestValue_thenReturnValue() {
        this.parameterScope.assignData("test", DataType.INT, 9L);
        assertEquals(9L, this.parameterScope.lookupData("test"));
        assertEquals(DataType.INT, this.parameterScope.lookupDataType("test"));
        assertEquals(new Data(DataType.INT, 9L), this.parameterScope.lookup("test"));
    }

    @Test
    void givenParameterScopeWithValueTest_whenAddTestValue_thenThrowException() {
        this.parameterScope.assignData("test", DataType.INT, 9L);
        assertThrows(ScopeException.class, () -> this.parameterScope.assignData("test", DataType.INT, 9L));
    }

    @Test
    void givenParameterScopeWithValueTest_whenUpdateValue_thenSuccess() {
        this.parameterScope.assignData("test", DataType.INT, 9L);
        this.parameterScope.updateData("test", 2L);
        assertEquals(2L, this.parameterScope.lookupData("test"));
    }

    @Test
    void givenParameterScope_whenUpdateValue_thenThrowException() {
        assertThrows(ScopeException.class, () -> this.parameterScope.updateData("test", 9L));
    }

}
