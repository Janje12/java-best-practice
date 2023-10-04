package com.solutions.todolist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public final class FindTodosRequest {
    @Min(1)
    @Schema(defaultValue = "10")
    private int pageSize = 10;
    @Min(0)
    @Schema(defaultValue = "0")
    private int pageNumber = 0;
    @Pattern(regexp = "^(asc|desc)$")
    @Schema(defaultValue = "asc")
    private String sortDirection = "asc";
    @Schema(example = "id", defaultValue = "id")
    private String sortBy = "id";

    public FindTodosRequest() {

    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(final String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(final String sortBy) {
        this.sortBy = sortBy;
    }
}
