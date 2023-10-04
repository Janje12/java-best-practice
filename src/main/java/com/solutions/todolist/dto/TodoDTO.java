package com.solutions.todolist.dto;

import java.time.Instant;

public record TodoDTO(Long id,
                      String title,
                      String description,
                      String state,
                      Instant createdAt,
                      Instant updatedAt){
}

