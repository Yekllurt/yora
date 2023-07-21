package dev.yekllurt.api.errors;

import dev.yekllurt.api.throwable.CompilationException;

public record Error(int code, Class<? extends CompilationException> exception, String message, String explanation) {

}
