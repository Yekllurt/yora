package dev.yekllurt.parser.interpreter.nativ.variable.impl;

import dev.yekllurt.parser.interpreter.nativ.variable.NativeVariable;

public class JavaVmVendorNativeVariable implements NativeVariable {

    @Override
    public Object getValue() {
        return System.getProperty("java.vm.specification.vendor");
    }

}
