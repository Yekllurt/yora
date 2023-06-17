package dev.yekllurt.parser.interpreter.nativ.variable.impl;

import dev.yekllurt.parser.interpreter.nativ.variable.NativeVariable;

public class OsArchNativeVariable implements NativeVariable {

    @Override
    public Object getValue() {
        return System.getProperty("os.arch");
    }

}
