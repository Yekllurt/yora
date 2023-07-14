package dev.yekllurt.parser.interpreter.scope;

import dev.yekllurt.api.DataType;

public interface DataScope {

    void assignData(String name, DataType type, Object value);

    void updateData(String name, Object value);

    Data lookup(String name);

    Object lookupData(String name);

    DataType lookupDataType(String name);

    boolean existsData(String name);

}
