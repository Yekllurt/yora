package dev.yekllurt.scanner.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtility {

    public static String readFile(File file) {
        try {
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFileEnding(File file) {
        if (file.getName().contains(".")) {
            return file.getName().substring(file.getName().lastIndexOf(".") + 1);
        } else {
            return file.getName();
        }
    }

}
