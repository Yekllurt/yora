package dev.yekllurt.api.utility;

import java.io.File;

public class SystemUtility {

    private SystemUtility() {
        // Intentional: other classes should not instantiate a helper class
    }

    public static int getMajorJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }

    public static boolean isRunningFromJar() {
        // https://stackoverflow.com/a/32766003
        return new File(SystemUtility.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName().contains(".jar");
    }

}
