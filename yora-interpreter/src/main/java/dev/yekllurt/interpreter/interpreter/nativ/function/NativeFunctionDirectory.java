package dev.yekllurt.interpreter.interpreter.nativ.function;

import dev.yekllurt.interpreter.interpreter.nativ.function.impl.*;

import java.util.HashMap;
import java.util.Map;

public class NativeFunctionDirectory {

    private static final Map<String, NativeFunction> NATIVE_FUNCTION_MAP = new HashMap<>();

    static {
        NATIVE_FUNCTION_MAP.put("println", new PrintlnNativeFunction());
        NATIVE_FUNCTION_MAP.put("readln", new ReadlnNativeFunction());
        NATIVE_FUNCTION_MAP.put("sqrt", new SqrtNativeFunction());
        NATIVE_FUNCTION_MAP.put("cos", new CosNativeFunction());
        NATIVE_FUNCTION_MAP.put("sin", new SinNativeFunction());
        NATIVE_FUNCTION_MAP.put("randl", new RandlNativeFunction());
    }

    private NativeFunctionDirectory() {
        // Intentional: other classes should not instantiate this class
    }

    public static NativeFunction getNativeFunction(String identifier) {
        return NATIVE_FUNCTION_MAP.get(identifier);
    }

    public static boolean isNativeFunction(String identifier) {
        return NATIVE_FUNCTION_MAP.containsKey(identifier);
    }

}
