package dev.yekllurt.parser.interpreter.nativefunction;

import dev.yekllurt.parser.interpreter.nativefunction.impl.PrintNativeFunction;

import java.util.HashMap;
import java.util.Map;

public class NativeFunctionDirectory {

    private static final Map<String, NativeFunction> NATIVE_FUNCTION_MAP = new HashMap<>();

    private NativeFunctionDirectory() {
        NATIVE_FUNCTION_MAP.put("print", new PrintNativeFunction());
    }

    public static NativeFunction getNativeFunction(String identifier) {
        return NATIVE_FUNCTION_MAP.get(identifier);
    }

    public static boolean isNativeFunction(String identifier) {
        return NATIVE_FUNCTION_MAP.containsKey(identifier);
    }

}
