package dev.yekllurt.interpreter.interpreter.nativ.variable.impl;

import dev.yekllurt.interpreter.interpreter.nativ.variable.NativeVariable;

public class PiNativeVariable implements NativeVariable {

    @Override
    public Object getValue() {
        return 3.1415926535f;
    }

}
