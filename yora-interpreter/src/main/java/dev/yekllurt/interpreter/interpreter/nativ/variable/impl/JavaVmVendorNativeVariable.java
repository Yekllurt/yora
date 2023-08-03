package dev.yekllurt.interpreter.interpreter.nativ.variable.impl;

import dev.yekllurt.interpreter.interpreter.nativ.variable.NativeVariable;

public class JavaVmVendorNativeVariable implements NativeVariable {

    @Override
    public Object getValue() {
        return System.getProperty("java.vm.specification.vendor");
    }

}
