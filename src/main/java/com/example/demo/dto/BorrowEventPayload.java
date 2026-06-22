package com.example.demo.dto;

public record BorrowEventPayload(
        Metadata metadata,

        ReturnCreatedEventData data
) {
}