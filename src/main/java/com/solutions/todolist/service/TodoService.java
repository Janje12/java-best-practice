package com.solutions.todolist.service;

import com.solutions.todolist.dto.FindTodosRequest;
import com.solutions.todolist.exception.TodoNotFoundException;
import com.solutions.todolist.model.Todo;
import com.solutions.todolist.repository.TodoRepository;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "todos")
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository repository;

    @Autowired
    public TodoService(final TodoRepository repository) {
        this.repository = repository;
    }

    public Page<Todo> findAll(final FindTodosRequest findTodosRequest) {
        return repository.findAll(PageRequest.of(findTodosRequest.getPageNumber(), findTodosRequest.getPageSize(),
                Sort.by(Direction.valueOf(findTodosRequest.getSortDirection().toUpperCase()), findTodosRequest.getSortBy())));
    }

    @Cacheable(key = "#id")
    public Todo findById(final Long id) {
        return repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    @Transactional
    public Todo save(final Todo todo) {
        return repository.save(todo);
    }

    @Transactional
    @CachePut(key = "#todo.id")
    public Todo update(final Todo todo) {
        todo.setUpdatedAt(Instant.now());
        return repository.save(todo);
    }

    @Transactional
    @CacheEvict(key = "#id")
    public void delete(final Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
