package dev.yekllurt.interpreter.interpreter.nativ.variable.impl;

import dev.yekllurt.interpreter.interpreter.nativ.variable.NativeVariable;

public class JavaVersionNativeVariable implements NativeVariable {

    @Override
    public Object getValue() {
        return System.getProperty("java.version");
    }

}
