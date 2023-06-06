package dev.yekllurt.lexer;

import dev.yekllurt.lexer.token.Token;
import dev.yekllurt.lexer.token.TokenDefinition;
import dev.yekllurt.lexer.token.TokenDefinitionLoader;
import dev.yekllurt.lexer.utility.FileUtility;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TokenDefinitionLoader tokenDefinitionLoader = new TokenDefinitionLoader();
        List<TokenDefinition> tokenDefinitionList = tokenDefinitionLoader.load(new File("./examples/lexer/lexer-definition.yora-lexer"));
        Lexer lexer = new Lexer(FileUtility.readFile(new File("./examples/scanner/test-program.yora.out")), tokenDefinitionList);
        List<Token> tokenList = lexer.tokenize();
        tokenList.forEach(token -> System.out.printf("%s%s%n", token.getType(), token.getValue() != null ? " " + token.getValue() : ""));
    }

}