package dev.yekllurt.parser.scope;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.throwable.execution.ScopeException;
import dev.yekllurt.parser.interpreter.scope.Data;
import dev.yekllurt.parser.interpreter.scope.ReturnScope;
import dev.yekllurt.parser.interpreter.scope.impl.ReturnScopeImplementation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ReturnScopeTest {

    private ReturnScope returnScope;

    @BeforeEach
    void beforeEach() {
        this.returnScope = new ReturnScopeImplementation();
    }

    @AfterEach
    void afterEach() {
        this.returnScope = null;
    }

    @Test
    void givenReturnScope_whenAddNoReturnValue_thenReturnValueIsNull() {
        assertNull(this.returnScope.lookup());
        assertNull(this.returnScope.lookupReturnValue());
        assertNull(this.returnScope.lookupReturnValueType());
    }

    @Test
    void givenReturnScopeWithReturnValue_whenAddReturnValue_thenThrowException() {
        this.returnScope.assignReturnValue(DataType.INT, 9L);
        assertThrows(ScopeException.class, () -> this.returnScope.assignReturnValue(DataType.INT, 9L));
    }

    @ParameterizedTest
    @MethodSource("provideAssignReturnValues")
    void givenReturnScope_whenAddReturnValue_thenReturnValue(DataType dataType, Object value) {
        this.returnScope.assignReturnValue(dataType, value);
        assertEquals(new Data(dataType, value), this.returnScope.lookup());
        assertEquals(value, this.returnScope.lookupReturnValue());
        assertEquals(dataType, this.returnScope.lookupReturnValueType());
    }

    private static Stream<Arguments> provideAssignReturnValues() {
        return Stream.of(
                Arguments.of(DataType.INT, 9L),
                Arguments.of(DataType.FLOAT, 9.9d),
                Arguments.of(DataType.BOOLEAN, true),
                Arguments.of(DataType.STRING, "Hello World!"),
                Arguments.of(DataType.INT_ARRAY, new long[]{2L, 9L, 42L}),
                Arguments.of(DataType.FLOAT_ARRAY, new double[]{2.2d, 9.9d, 42.42d}),
                Arguments.of(DataType.BOOLEAN_ARRAY, new boolean[]{true, false, true}),
                Arguments.of(DataType.STRING_ARRAY, new String[]{"Hello", "World", "!"})
        );
    }

}
