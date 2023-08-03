package dev.yekllurt.interpreter.interpreter.nativ.variable.impl;

import dev.yekllurt.interpreter.interpreter.nativ.variable.NativeVariable;

public class JavaVmVersionNativeVariable implements NativeVariable {

    @Override
    public Object getValue() {
        return System.getProperty("java.vm.specification.version");
    }

}
