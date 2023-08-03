package dev.yekllurt.interpreter.interpreter.scope;

public interface ParameterScope extends DataScope {

    default String getScopeName() {
        return "parameter";
    }

}
