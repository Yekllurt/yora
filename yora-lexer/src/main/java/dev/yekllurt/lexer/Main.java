package dev.yekllurt.lexer;

import dev.yekllurt.lexer.token.TokenDefinitionLoader;
import dev.yekllurt.lexer.utility.FileUtility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IOException {

        if (!(args.length == 2 || args.length == 3)) {
            System.out.println("Invalid argument length. Program arguments = <token-definition-file> <program-file> <token-output-file>");
            System.exit(1);
        }

        var tokenDefinitionFile = args[0];
        var programFile = args[1];
        var tokenOutputFile = args.length == 3 ? args[2] : null;

        var tokenDefinitionList = new TokenDefinitionLoader().load(new File(tokenDefinitionFile));
        var lexer = new Lexer(FileUtility.readFile(new File(programFile)), tokenDefinitionList);
        var tokens = lexer.tokenize();

        if (Objects.nonNull(tokenOutputFile)) {
            try (var writer = new BufferedWriter(new FileWriter(tokenOutputFile))) {
                for (var token : tokens) {
                    writer.write(String.format("%s%s%n", token.getType(), token.getValue() != null ? " " + token.getValue() : ""));
                }
            }
        } else {
            tokens.forEach(token -> System.out.printf("%s%s%n", token.getType(), token.getValue() != null ? " " + token.getValue() : ""));
        }
    }

}