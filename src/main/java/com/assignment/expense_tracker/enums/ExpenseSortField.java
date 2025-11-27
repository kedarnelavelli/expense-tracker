package com.assignment.expense_tracker.enums;

public enum ExpenseSortField {
    DATE("date"),
    AMOUNT("amount"),
    NONE("");

    private final String field;

    ExpenseSortField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
