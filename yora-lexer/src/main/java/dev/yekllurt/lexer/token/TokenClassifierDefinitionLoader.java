package dev.yekllurt.lexer.token;

import dev.yekllurt.lexer.throwable.LexerException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TokenClassifierDefinitionLoader {

    private static final String FILE_SECTION_SPLITTER = "###";
    private static final int FILE_SECTION_PATTERN_DEFINITION = 0;
    private static final int FILE_SECTION_TOKEN_DEFINITION = 1;

    public List<TokenClassifierDefinition> load(File file) {
        int fileSection = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            var patternDefinitions = new HashMap<String, String>();
            var tokenDefinitions = new LinkedList<TokenClassifierDefinition>();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("")) {
                    continue;
                }
                if (line.equals(FILE_SECTION_SPLITTER)) {
                    fileSection++;
                    continue;
                }

                switch (fileSection) {
                    case FILE_SECTION_PATTERN_DEFINITION -> performPatternDefinitionOperation(line, patternDefinitions);
                    case FILE_SECTION_TOKEN_DEFINITION ->
                            performTokenDefinitionOperation(line, patternDefinitions, tokenDefinitions);
                    default -> throw new LexerException("Unknown lexer file section");
                }
            }
            return tokenDefinitions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void performPatternDefinitionOperation(String line, Map<String, String> patternDefinitions) {
        var lineData = line.split("\s+", 2);
        patternDefinitions.put(lineData[0].trim(), lineData[1].trim());
    }

    private void performTokenDefinitionOperation(String line, Map<String, String> patternDefinitions, LinkedList<TokenClassifierDefinition> tokenClassifierDefinitions) {
        var lineData = line.split("\s+", 4);
        var pattern = lineData[1].trim();
        if (getFirstChar(pattern) == '"' && getLastChar(pattern) == '"') {
            var shortenedPattern = pattern.substring(1, pattern.length() - 1);
            if (shortenedPattern.length() == 1) {
                var prefix = "";
                if (Set.of("^").contains(shortenedPattern)) {
                    prefix += "\\";
                }
                pattern = "[" + prefix + shortenedPattern + "]";
            } else {
                pattern = "(" + shortenedPattern + ")";
            }
        } else if (getFirstChar(pattern) == '{' && getLastChar(pattern) == '}') {
            pattern = patternDefinitions.get(pattern.substring(1, pattern.length() - 1));
        } else {
            throw new LexerException(String.format("Unknown pattern definitions '%s' for a token definition", pattern));
        }

        tokenClassifierDefinitions.add(TokenClassifierDefinition.builder()
                .category(lineData[0].trim())
                .pattern(pattern)
                .keepValue(!lineData[2].trim().equals("0"))
                .name(lineData[3].trim())
                .build());
    }

    private char getFirstChar(String s) {
        return s.charAt(0);
    }

    private char getLastChar(String s) {
        return s.charAt(s.length() - 1);
    }

}
