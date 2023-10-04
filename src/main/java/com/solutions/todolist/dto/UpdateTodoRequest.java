package com.solutions.todolist.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateTodoRequest(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotBlank
        String state) {
}

