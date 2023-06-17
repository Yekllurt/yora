package dev.yekllurt.parser.interpreter.scope;

import dev.yekllurt.parser.ast.impl.FunctionNode;

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
