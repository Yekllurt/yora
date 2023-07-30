package dev.yekllurt.lexer.token;

import dev.yekllurt.api.errors.LexicalError;
import dev.yekllurt.api.utility.ExceptionUtility;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class TokenScanDefinitionLoader {

    public List<TokenScanDefinition> load(Reader reader, String sourceName) {
        try (var bufferedReader = new BufferedReader(reader)) {
            var result = new LinkedList<TokenScanDefinition>();
            var lineCount = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lineCount++;
                var lineData = line.split("\s+", 3);
                ExceptionUtility.throwExceptionIf(lineData.length != 3, LexicalError.INVALID_TOKEN_DEFINITION_LINE,
                        lineCount, sourceName);
                result.add(TokenScanDefinition.builder()
                        .name(lineData[0].trim())
                        .ignore(!lineData[1].trim().equals("0"))
                        .pattern(lineData[2].trim())
                        .build());
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
