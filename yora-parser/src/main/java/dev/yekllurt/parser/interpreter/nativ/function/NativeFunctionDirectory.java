package dev.yekllurt.parser.interpreter.nativ.function;

import dev.yekllurt.parser.interpreter.nativ.function.impl.*;

import java.util.HashMap;
import java.util.Map;

public class NativeFunctionDirectory {

    private static final Map<String, NativeFunction> NATIVE_FUNCTION_MAP = new HashMap<>();

    static {
        NATIVE_FUNCTION_MAP.put("print", new PrintNativeFunction());
        NATIVE_FUNCTION_MAP.put("sqrt", new SqrtNativeFunction());
        NATIVE_FUNCTION_MAP.put("cos", new CosNativeFunction());
        NATIVE_FUNCTION_MAP.put("sin", new SinNativeFunction());
        NATIVE_FUNCTION_MAP.put("randl", new RandomLongNativeFunction());
    }

    private NativeFunctionDirectory() {
        // Intentional as this class should not be instantiated by other classes
    }

    public static NativeFunction getNativeFunction(String identifier) {
        return NATIVE_FUNCTION_MAP.get(identifier);
    }

    public static boolean isNativeFunction(String identifier) {
        return NATIVE_FUNCTION_MAP.containsKey(identifier);
    }

}
