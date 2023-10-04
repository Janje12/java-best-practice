package com.solutions.todolist.mapper;

import com.solutions.todolist.dto.CreateTodoRequest;
import com.solutions.todolist.dto.TodoDTO;
import com.solutions.todolist.dto.PartialUpdateTodoRequest;
import com.solutions.todolist.dto.UpdateTodoRequest;
import com.solutions.todolist.model.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TodoMapper {
    Todo mapToModel(final CreateTodoRequest createTodoRequest);

    Todo fullMapToModel(final UpdateTodoRequest updateTodoRequest, @MappingTarget Todo todoToUpdate);

    Todo partialMapToModel(final PartialUpdateTodoRequest partialUpdateTodoRequest, @MappingTarget Todo todoToUpdate);

    TodoDTO mapToDTO(final Todo todo);
}
