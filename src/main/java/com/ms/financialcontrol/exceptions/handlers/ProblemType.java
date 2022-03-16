package com.ms.financialcontrol.exceptions.handlers;

import lombok.Getter;

@Getter
public enum ProblemType {

    INVALID_DATA("/invalid-data", "Invalid data"),
    ACCESS_DENIED("/access-denied", "Access denied"),
    SYSTEM_ERROR("/system-error", "System error"),
    INVALID_PARAM("/invalid-param", "Invalid param"),
    UNREADABLE_MESSAGE("/unreadable-message", "Unreadable message"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    ENTITY_IN_USE("/entity-in-use", "Entity in use"),
    BUSINESS_ERROR("/business-error", "Business error"),
    CONFLICT_DATA("/conflict-data", "Conflict data");

    private final String title;
    private final String uri;

    ProblemType(String path, String title) {
        this.uri = "https://localhost:8080" + path;
        this.title = title;
    }
}
