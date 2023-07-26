package dev.yekllurt.lexer;

import dev.yekllurt.api.throwable.compilation.LexicalException;
import dev.yekllurt.api.utility.FileUtility;
import dev.yekllurt.lexer.token.TokenClassifierDefinitionLoader;
import dev.yekllurt.lexer.token.TokenScanDefinitionLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        try {
            if (args.length != 2) {
                System.err.println("Invalid argument length. Expecting two arguments.");
                System.exit(1);
            }

            var programFile = args[0];
            var tokenOutputFile = args[1];

            // Perform token scan
            var tokenScanDefinitionList = new TokenScanDefinitionLoader().load(new File("./specification/token-scan.definition"));
            var tokenScanner = new TokenScanner(FileUtility.readFile(new File(programFile)), tokenScanDefinitionList);
            var scannerTokens = tokenScanner.tokenize();

            // Perform token classifier
            var tokenClassifierDefinitionList = new TokenClassifierDefinitionLoader().load(new File("./specification/token-classifier.definition"));
            var tokenClassifier = new TokenClassifier(scannerTokens, tokenClassifierDefinitionList);
            var classifierTokens = tokenClassifier.tokenize();

            try (var writer = new BufferedWriter(new FileWriter(tokenOutputFile))) {
                for (var token : classifierTokens) {
                    writer.write(String.format("%s%s%n", token.getType(), token.getValue() != null ? " " + token.getValue() : ""));
                }
            }
        } catch (LexicalException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

}
