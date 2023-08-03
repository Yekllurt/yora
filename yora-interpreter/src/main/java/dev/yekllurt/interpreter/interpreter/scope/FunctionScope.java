package dev.yekllurt.interpreter.interpreter.scope;

import dev.yekllurt.interpreter.ast.impl.FunctionNode;

public interface FunctionScope extends Scope {

    /**
     * Create a new function in the current scope
     *
     * @param name     the function name
     * @param function the function
     */
    void assignFunction(String name, FunctionNode function);

    /**
     * Lookup a function in the current scope
     *
     * @param name the function name
     * @return the function
     */
    FunctionNode lookupFunction(String name);

}
