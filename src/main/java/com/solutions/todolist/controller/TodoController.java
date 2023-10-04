package com.solutions.todolist.controller;

import com.solutions.todolist.dto.CreateTodoRequest;
import com.solutions.todolist.dto.FindTodosRequest;
import com.solutions.todolist.dto.PagedResult;
import com.solutions.todolist.dto.TodoDTO;
import com.solutions.todolist.dto.PartialUpdateTodoRequest;
import com.solutions.todolist.dto.UpdateTodoRequest;
import com.solutions.todolist.mapper.TodoMapper;
import com.solutions.todolist.model.Todo;
import com.solutions.todolist.service.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Validated
@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService service;
    private final TodoMapper mapper = Mappers.getMapper(TodoMapper.class);

    @Autowired
    public TodoController(final TodoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PagedResult<TodoDTO>> getTodos(
            @Valid FindTodosRequest findTodosRequest) {
        final Page<Todo> todoPage = service.findAll(findTodosRequest);
        return ResponseEntity.ok(new PagedResult<>(
                todoPage.getContent().stream().map(mapper::mapToDTO).toList(),
                todoPage.getTotalElements(),
                todoPage.getNumber(),
                todoPage.getTotalPages(),
                todoPage.isFirst(),
                todoPage.isLast(),
                todoPage.hasNext(),
                todoPage.hasPrevious()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getTodo(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(mapper.mapToDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<TodoDTO> createTodo(@RequestBody @Valid CreateTodoRequest createTodoRequest) {
        final Todo todo = service.save(mapper.mapToModel(createTodoRequest));
        return ResponseEntity.created(buildLocation(todo)).body(mapper.mapToDTO(todo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDTO> updateTodo(@PathVariable @Min(1) Long id, @RequestBody @Valid UpdateTodoRequest updateTodoRequest) {
        final Todo foundTodo = service.findById(id);
        final Todo todo = service.update(mapper.fullMapToModel(updateTodoRequest, foundTodo));
        return ResponseEntity.ok().location(buildLocation(todo)).body(mapper.mapToDTO(todo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TodoDTO> partialUpdateTodo(@PathVariable @Min(1) Long id, @RequestBody @Valid PartialUpdateTodoRequest partialUpdateTodoRequest) {
        final Todo foundTodo = service.findById(id);
        final Todo todo = service.update(mapper.partialMapToModel(partialUpdateTodoRequest, foundTodo));
        return ResponseEntity.ok().location(buildLocation(todo)).body(mapper.mapToDTO(todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TodoDTO> updateTodo(@PathVariable @Min(1) Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static URI buildLocation(final Todo todo) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(todo.getId()).toUri();
    }
}
