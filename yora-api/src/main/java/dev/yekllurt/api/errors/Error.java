package dev.yekllurt.api.errors;

public record Error(int code, Class<? extends RuntimeException> exception, String message) {

}
