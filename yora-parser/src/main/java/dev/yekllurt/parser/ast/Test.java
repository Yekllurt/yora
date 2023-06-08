package dev.yekllurt.parser.ast;

import dev.yekllurt.parser.ast.creator.Parser;
import dev.yekllurt.parser.interpreter.scope.VariableScope;
import dev.yekllurt.parser.interpreter.scope.impl.VariableScopeImplementation;
import dev.yekllurt.parser.token.TokenLoader;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        var tokens = new TokenLoader().load(new File("./examples/lexer/test-program.yora.out"));
        var parser = new Parser(tokens);
        var node = parser.parse();
        int x = 0;
        VariableScope variableScope = new VariableScopeImplementation();
        variableScope.beginScope();
        node.evaluate(variableScope, null, null);
        variableScope.endScope();
    }

}
