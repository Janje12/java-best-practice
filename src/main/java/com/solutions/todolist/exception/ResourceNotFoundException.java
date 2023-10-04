package com.solutions.todolist.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final String value;

    public String getValue() {
        return value;
    }

    public ResourceNotFoundException(final String value) {
        super(String.format("Resource with value %s not found.", value));
        this.value = value;
    }

    public ResourceNotFoundException(final String message, final String value) {
        super(message);
        this.value = value;
    }
}
