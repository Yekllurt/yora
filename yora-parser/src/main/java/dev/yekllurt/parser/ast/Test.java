package dev.yekllurt.parser.ast;

import dev.yekllurt.parser.ast.creator.Parser;
import dev.yekllurt.parser.interpreter.scope.FunctionScope;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.FunctionScopeImplementation;
import dev.yekllurt.parser.interpreter.scope.impl.VariableScopeImplementation;
import dev.yekllurt.parser.token.TokenLoader;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        var a = System.getenv();
        var b = System.getProperties();
        var tokens = new TokenLoader().load(new File("./examples/lexer/test-program.yora.out"));
        var parser = new Parser(tokens);
        var program = parser.parse();
        FunctionScope functionScope = new FunctionScopeImplementation();
        functionScope.beginScope();
        VariableScope variableScope = new VariableScopeImplementation();
        variableScope.beginScope();
        program.evaluate(functionScope, variableScope, null, null);
        functionScope.endScope();
        variableScope.endScope();
    }

}
