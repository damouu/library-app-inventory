package com.example.demo.dto;

public record BorrowCreatedEvent(
        Metadata metadata,

        BorrowCreatedEventData data
) {
}
