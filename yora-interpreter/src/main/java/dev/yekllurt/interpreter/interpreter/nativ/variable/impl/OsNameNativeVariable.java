package dev.yekllurt.interpreter.interpreter.nativ.variable.impl;

import dev.yekllurt.interpreter.interpreter.nativ.variable.NativeVariable;

public class OsNameNativeVariable implements NativeVariable {

    @Override
    public Object getValue() {
        return System.getProperty("os.name");
    }

}
