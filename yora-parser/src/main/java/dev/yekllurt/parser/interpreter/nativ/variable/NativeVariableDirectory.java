package dev.yekllurt.parser.interpreter.nativ.variable;

import dev.yekllurt.parser.interpreter.nativ.variable.impl.ENativeVariable;
import dev.yekllurt.parser.interpreter.nativ.variable.impl.OsArchNativeVariable;
import dev.yekllurt.parser.interpreter.nativ.variable.impl.OsNameNativeVariable;
import dev.yekllurt.parser.interpreter.nativ.variable.impl.PiNativeVariable;

import java.util.HashMap;
import java.util.Map;

public class NativeVariableDirectory {

    private static final Map<String, NativeVariable> NATIVE_VARIABLE_MAP = new HashMap<>();

    static {
        NATIVE_VARIABLE_MAP.put("pi", new PiNativeVariable());
        NATIVE_VARIABLE_MAP.put("e", new ENativeVariable());
        NATIVE_VARIABLE_MAP.put("os_arch", new OsArchNativeVariable());
        NATIVE_VARIABLE_MAP.put("os_name", new OsNameNativeVariable());
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
