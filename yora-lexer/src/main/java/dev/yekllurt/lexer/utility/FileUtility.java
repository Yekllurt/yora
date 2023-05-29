package dev.yekllurt.lexer.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtility {

    private FileUtility() {
        // Intentional as a helper class should not be instantiated by other classes
    }

    public static String readFile(File file) {
        try {
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}