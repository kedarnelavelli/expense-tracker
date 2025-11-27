package com.assignment.expense_tracker.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private OffsetDateTime timestamp;
    private int status;
    private String message;
    private List<String> errors;
}

