package dev.yekllurt.parser.utility;

public class SystemUtility {

    private SystemUtility() {
        // Intentional as a helper class should not be instantiated by other classes
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

}
