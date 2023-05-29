package dev.yekllurt.parser;

import dev.yekllurt.parser.token.Token;
import dev.yekllurt.parser.token.TokenDefinition;
import dev.yekllurt.parser.token.TokenDefinitionLoader;
import dev.yekllurt.parser.utility.FileUtility;

import java.io.File;
import java.util.List;

public class Main {

    // Creates a list of tokens
    public static void main(String[] args) {
        TokenDefinitionLoader tokenDefinitionLoader = new TokenDefinitionLoader();
        List<TokenDefinition> tokenDefinitionList = tokenDefinitionLoader.load(new File("./examples/token-definition.yora-scanner"));
        Lexer lexer = new Lexer(FileUtility.readFile(new File("./examples/test-program.yora")), tokenDefinitionList);
        List<Token> tokenList = lexer.tokenize();
        tokenList.forEach(System.out::println);
    }

}