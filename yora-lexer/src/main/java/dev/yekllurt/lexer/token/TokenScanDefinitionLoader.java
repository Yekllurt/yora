package dev.yekllurt.lexer.token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TokenScanDefinitionLoader {

    public List<TokenScanDefinition> load(File file) {
        try (var reader = new BufferedReader(new FileReader(file))) {
            var result = new LinkedList<TokenScanDefinition>();
            String line;
            while ((line = reader.readLine()) != null) {
                var lineData = line.split("\s+", 3);
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
