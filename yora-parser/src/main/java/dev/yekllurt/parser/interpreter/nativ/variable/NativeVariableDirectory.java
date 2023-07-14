package dev.yekllurt.parser.interpreter.nativ.variable;

import dev.yekllurt.parser.interpreter.nativ.variable.impl.*;

import java.util.HashMap;
import java.util.Map;

public class NativeVariableDirectory {

    private static final Map<String, NativeVariable> NATIVE_VARIABLE_MAP = new HashMap<>();

    static {
        NATIVE_VARIABLE_MAP.put("PI", new PiNativeVariable());
        NATIVE_VARIABLE_MAP.put("E", new ENativeVariable());
        NATIVE_VARIABLE_MAP.put("OSARCH", new OsArchNativeVariable());
        NATIVE_VARIABLE_MAP.put("OSNAME", new OsNameNativeVariable());
        NATIVE_VARIABLE_MAP.put("JAVAVERSION", new JavaVersionNativeVariable());
        NATIVE_VARIABLE_MAP.put("JAVAVMVERSION", new JavaVmVersionNativeVariable());
        NATIVE_VARIABLE_MAP.put("JAVAVMVENDOR", new JavaVmVendorNativeVariable());
    }

    private NativeVariableDirectory() {
        // Intentional as this class should not be instantiated by other classes
    }

    public static NativeVariable getNativeVariable(String identifier) {
        return NATIVE_VARIABLE_MAP.get(identifier);
    }

    public static boolean isNativeVariable(String identifier) {
        return NATIVE_VARIABLE_MAP.containsKey(identifier);
    }

}
