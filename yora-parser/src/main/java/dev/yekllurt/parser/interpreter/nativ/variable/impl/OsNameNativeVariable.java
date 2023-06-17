package dev.yekllurt.parser.interpreter.nativ.variable.impl;

import dev.yekllurt.parser.interpreter.nativ.variable.NativeVariable;

public class OsNameNativeVariable implements NativeVariable {

    @Override
    public Object getValue() {
        return System.getProperty("os.name");
    }

}
