package dev.yekllurt.parser.interpreter.scope;

public interface ParameterScope extends DataScope {

    default String getScopeName() {
        return "parameter";
    }

}
