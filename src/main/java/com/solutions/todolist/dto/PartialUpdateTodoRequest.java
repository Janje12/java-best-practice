package com.solutions.todolist.dto;

public record PartialUpdateTodoRequest(
        String title,
        String description,
        String state) {
}

