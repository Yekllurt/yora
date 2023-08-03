package dev.yekllurt.interpreter.program;

import dev.yekllurt.interpreter.Interpreter;
import dev.yekllurt.interpreter.token.TokenLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private TokenLoader tokenLoader;

    @BeforeEach
    void beforeEach() {
        System.setOut(new PrintStream(this.outContent));
        System.setErr(new PrintStream(this.errContent));
        this.tokenLoader = new TokenLoader();
    }

    @AfterEach
    void afterEach() {
        this.tokenLoader = null;
        System.setOut(this.originalOut);
        System.setErr(this.originalErr);
    }

    @ParameterizedTest
    @MethodSource("providePrograms")
    void testProgram(String programName, String program, String expectedOutput) {
        var tokens = new TokenLoader().load(new StringReader(program));
        Interpreter.interpret(tokens);
        assertEquals(expectedOutput, outContent.toString());
    }

    private static Stream<Arguments> providePrograms() {
        return Stream.of(programHelloWorld(),
                programBubbleSort(),
                programNativeFunctions(),
                programNativeVariables(),
                programScope(),
                programWhile());
    }

    private static Arguments programHelloWorld() {
        return Arguments.of("Hello world",
                """
                        VOID
                        IDENTIFIER main
                        LEFT_BRACE
                        RIGHT_BRACE
                        IDENTIFIER println
                        LEFT_BRACE
                        STRING "Hello World!"
                        RIGHT_BRACE
                        SEMICOLON
                        END
                        SEMICOLON
                        """,
                "Hello World!" + System.lineSeparator());
    }

    private static Arguments programBubbleSort() {
        return Arguments.of("Bubble sort",
                """
                        VOID
                        IDENTIFIER main
                        LEFT_BRACE
                        RIGHT_BRACE
                        INT
                        IDENTIFIER arraySize
                        EQUAL
                        NUMBER 5
                        SEMICOLON
                        INT
                        LEFT_BRACKET
                        NUMBER 5
                        RIGHT_BRACKET
                        IDENTIFIER a
                        SEMICOLON
                        INT
                        IDENTIFIER i
                        EQUAL
                        NUMBER 0
                        SEMICOLON
                        INT
                        IDENTIFIER j
                        EQUAL
                        NUMBER 0
                        SEMICOLON
                        IDENTIFIER a
                        LEFT_BRACKET
                        NUMBER 0
                        RIGHT_BRACKET
                        EQUAL
                        NUMBER 9
                        SEMICOLON
                        IDENTIFIER a
                        LEFT_BRACKET
                        NUMBER 1
                        RIGHT_BRACKET
                        EQUAL
                        NUMBER 2
                        SEMICOLON
                        IDENTIFIER a
                        LEFT_BRACKET
                        NUMBER 2
                        RIGHT_BRACKET
                        EQUAL
                        NUMBER 5
                        SEMICOLON
                        IDENTIFIER a
                        LEFT_BRACKET
                        NUMBER 3
                        RIGHT_BRACKET
                        EQUAL
                        NUMBER 3
                        SEMICOLON
                        IDENTIFIER a
                        LEFT_BRACKET
                        NUMBER 4
                        RIGHT_BRACKET
                        EQUAL
                        NUMBER 6
                        SEMICOLON
                        IDENTIFIER println
                        LEFT_BRACE
                        IDENTIFIER a
                        RIGHT_BRACE
                        SEMICOLON
                        IDENTIFIER i
                        EQUAL
                        NUMBER 0
                        SEMICOLON
                        WHILE
                        LEFT_BRACE
                        IDENTIFIER i
                        LESS_THAN
                        IDENTIFIER arraySize
                        MINUS
                        NUMBER 1
                        RIGHT_BRACE
                        WHILE
                        LEFT_BRACE
                        IDENTIFIER j
                        LESS_THAN
                        IDENTIFIER arraySize
                        MINUS
                        NUMBER 1
                        RIGHT_BRACE
                        IF
                        LEFT_BRACE
                        IDENTIFIER a
                        LEFT_BRACKET
                        IDENTIFIER j
                        RIGHT_BRACKET
                        GREATER_THAN
                        IDENTIFIER a
                        LEFT_BRACKET
                        IDENTIFIER j
                        PLUS
                        NUMBER 1
                        RIGHT_BRACKET
                        RIGHT_BRACE
                        INT
                        IDENTIFIER temp
                        EQUAL
                        IDENTIFIER a
                        LEFT_BRACKET
                        IDENTIFIER j
                        RIGHT_BRACKET
                        SEMICOLON
                        IDENTIFIER a
                        LEFT_BRACKET
                        IDENTIFIER j
                        RIGHT_BRACKET
                        EQUAL
                        IDENTIFIER a
                        LEFT_BRACKET
                        IDENTIFIER j
                        PLUS
                        NUMBER 1
                        RIGHT_BRACKET
                        SEMICOLON
                        IDENTIFIER a
                        LEFT_BRACKET
                        IDENTIFIER j
                        PLUS
                        NUMBER 1
                        RIGHT_BRACKET
                        EQUAL
                        IDENTIFIER temp
                        SEMICOLON
                        END
                        SEMICOLON
                        IDENTIFIER j
                        EQUAL
                        IDENTIFIER j
                        PLUS
                        NUMBER 1
                        SEMICOLON
                        END
                        SEMICOLON
                        IDENTIFIER i
                        EQUAL
                        IDENTIFIER i
                        PLUS
                        NUMBER 1
                        SEMICOLON
                        IDENTIFIER j
                        EQUAL
                        NUMBER 0
                        SEMICOLON
                        END
                        SEMICOLON
                        IDENTIFIER println
                        LEFT_BRACE
                        IDENTIFIER a
                        RIGHT_BRACE
                        SEMICOLON
                        END
                        SEMICOLON
                        """,
                "[9, 2, 5, 3, 6]" + System.lineSeparator() +
                        "[2, 3, 5, 6, 9]" + System.lineSeparator());
    }

    private static Arguments programNativeFunctions() {
        return Arguments.of("Native functions",
                """
                        VOID
                        IDENTIFIER main
                        LEFT_BRACE
                        RIGHT_BRACE
                        IDENTIFIER println
                        LEFT_BRACE
                        IDENTIFIER sin
                        LEFT_BRACE
                        NUMBER 1
                        RIGHT_BRACE
                        RIGHT_BRACE
                        SEMICOLON
                        IDENTIFIER println
                        LEFT_BRACE
                        IDENTIFIER cos
                        LEFT_BRACE
                        NUMBER 0
                        RIGHT_BRACE
                        RIGHT_BRACE
                        SEMICOLON
                        IDENTIFIER println
                        LEFT_BRACE
                        IDENTIFIER sqrt
                        LEFT_BRACE
                        NUMBER 9
                        RIGHT_BRACE
                        RIGHT_BRACE
                        SEMICOLON
                        IDENTIFIER println
                        LEFT_BRACE
                        IDENTIFIER sqrt
                        LEFT_BRACE
                        NUMBER 5
                        RIGHT_BRACE
                        RIGHT_BRACE
                        SEMICOLON
                        END
                        SEMICOLON
                        """,
                "0.8414709848078965" + System.lineSeparator() +
                        "1.0" + System.lineSeparator() +
                        "3.0" + System.lineSeparator() +
                        "2.23606797749979" + System.lineSeparator());
    }

    private static Arguments programNativeVariables() {
        return Arguments.of("Native variables",
                """
                        VOID
                        IDENTIFIER main
                        LEFT_BRACE
                        RIGHT_BRACE
                        IDENTIFIER println
                        LEFT_BRACE
                        STRING "PI: "
                        PLUS
                        IDENTIFIER PI
                        RIGHT_BRACE
                        SEMICOLON
                        IDENTIFIER println
                        LEFT_BRACE
                        STRING "E: "
                        PLUS
                        IDENTIFIER E
                        RIGHT_BRACE
                        SEMICOLON
                        END
                        SEMICOLON
                        """,
                "PI: 3.1415927" + System.lineSeparator() +
                        "E: 2.7182817" + System.lineSeparator());
    }

    private static Arguments programScope() {
        return Arguments.of("Scope",
                """
                        VOID
                        IDENTIFIER main
                        LEFT_BRACE
                        RIGHT_BRACE
                        INT
                        IDENTIFIER a
                        EQUAL
                        NUMBER 4
                        SEMICOLON
                        IDENTIFIER a
                        EQUAL
                        IDENTIFIER double
                        LEFT_BRACE
                        IDENTIFIER a
                        RIGHT_BRACE
                        SEMICOLON
                        IDENTIFIER println
                        LEFT_BRACE
                        IDENTIFIER a
                        RIGHT_BRACE
                        SEMICOLON
                        END
                        SEMICOLON
                        INT
                        IDENTIFIER double
                        LEFT_BRACE
                        INT
                        IDENTIFIER input
                        RIGHT_BRACE
                        INT
                        IDENTIFIER a
                        EQUAL
                        NUMBER 2
                        STAR
                        IDENTIFIER input
                        SEMICOLON
                        RETURN
                        IDENTIFIER a
                        SEMICOLON
                        END
                        SEMICOLON
                        """,
                "8" + System.lineSeparator());
    }

    private static Arguments programWhile() {
        return Arguments.of("While",
                """
                        VOID
                        IDENTIFIER main
                        LEFT_BRACE
                        RIGHT_BRACE
                        INT
                        IDENTIFIER a
                        EQUAL
                        NUMBER 4
                        SEMICOLON
                        WHILE
                        LEFT_BRACE
                        IDENTIFIER a
                        GREATER_THAN
                        EQUAL
                        NUMBER 0
                        RIGHT_BRACE
                        IF
                        LEFT_BRACE
                        IDENTIFIER a
                        PERCENT
                        NUMBER 2
                        EQUAL
                        EQUAL
                        NUMBER 0
                        RIGHT_BRACE
                        IDENTIFIER println
                        LEFT_BRACE
                        IDENTIFIER a
                        RIGHT_BRACE
                        SEMICOLON
                        END
                        SEMICOLON
                        IDENTIFIER a
                        EQUAL
                        IDENTIFIER a
                        MINUS
                        NUMBER 1
                        SEMICOLON
                        END
                        SEMICOLON
                        END
                        SEMICOLON
                        """,
                "4" + System.lineSeparator() +
                        "2" + System.lineSeparator() +
                        "0" + System.lineSeparator());
    }

}
