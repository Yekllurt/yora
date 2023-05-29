package dev.yekllurt.scanner;

import dev.yekllurt.scanner.token.Token;
import dev.yekllurt.scanner.token.TokenDefinition;
import dev.yekllurt.scanner.token.TokenDefinitionLoader;
import dev.yekllurt.scanner.utility.FileUtility;

import java.io.File;
import java.util.List;

public class Main {

    // Creates a list of tokens
    public static void main(String[] args) {
        TokenDefinitionLoader tokenDefinitionLoader = new TokenDefinitionLoader();
        List<TokenDefinition> tokenDefinitionList = tokenDefinitionLoader.load(new File("./examples/scanner/token-definition.yora-scanner"));
        Scanner scanner = new Scanner(FileUtility.readFile(new File("./examples/test-program.yora")), tokenDefinitionList);
        List<Token> tokenList = scanner.tokenize();
        tokenList.forEach(token -> System.out.println(String.format("%s %s", token.getType(), token.getValue())));
    }

}