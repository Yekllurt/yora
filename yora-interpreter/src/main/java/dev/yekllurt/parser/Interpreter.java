package dev.yekllurt.parser;

import dev.yekllurt.api.collection.SequencedCollection;
import dev.yekllurt.parser.ast.creator.Parser;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.FunctionScopeImplementation;
import dev.yekllurt.parser.interpreter.scope.impl.VariableScopeImplementation;
import dev.yekllurt.parser.token.Token;
import dev.yekllurt.parser.token.TokenLoader;
import dev.yekllurt.api.utility.SystemUtility;

import java.io.File;

public class Interpreter {

    public static void main(String[] args) {

        if (SystemUtility.getMajorJavaVersion() < 19) {
            System.err.printf("The interpreter has been designed for Java Version 19 and higher however you are using Java Version %s%n", SystemUtility.getMajorJavaVersion());
        }

        if (args.length != 1) {
            System.err.println("You must only provide one program");
            System.exit(1);
        }

        var programFile = args[0];

        var tokens = new TokenLoader().load(new File(programFile));
        interpret(tokens);
    }

    public static void interpret(SequencedCollection<Token> tokens) {
        var parser = new Parser(tokens);
        var program = parser.parse();
        FunctionScope functionScope = new FunctionScopeImplementation();
        functionScope.beginHardScope();
        functionScope.beginSoftScope();

        VariableScope variableScope = new VariableScopeImplementation();
        variableScope.beginHardScope();
        variableScope.beginSoftScope();
        // TODO: if a user should be able to pass arguments when calling a program then pass them here via parameterScope
        program.evaluate(functionScope, variableScope, null, null);
        variableScope.endSoftScope();
        variableScope.endHardScope();

        functionScope.endSoftScope();
        functionScope.endHardScope();
    }

}