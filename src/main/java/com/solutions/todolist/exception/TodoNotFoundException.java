package com.solutions.todolist.exception;

public class TodoNotFoundException extends ResourceNotFoundException {
    public TodoNotFoundException(final Long id) {
        super(String.format("Unable to find Todo with id %s", id), String.valueOf(id));
    }
}
