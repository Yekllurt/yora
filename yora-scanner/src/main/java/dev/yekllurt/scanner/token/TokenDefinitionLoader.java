package dev.yekllurt.scanner.token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TokenDefinitionLoader {

    public List<TokenDefinition> load(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<TokenDefinition> result = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineData = line.split("\s+", 3);
                result.add(TokenDefinition.builder()
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
