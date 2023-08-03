package dev.yekllurt.parser.interpreter.nativ.variable.impl;

import dev.yekllurt.parser.interpreter.nativ.variable.NativeVariable;

public class PiNativeVariable implements NativeVariable {

    @Override
    public Object getValue() {
        return 3.1415926535f;
    }

}
