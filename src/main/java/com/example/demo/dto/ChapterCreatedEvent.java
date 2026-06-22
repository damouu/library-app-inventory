package com.example.demo.dto;

public record ChapterCreatedEvent(
        Metadata metadata,

        ChapterCreatedEventData data
) {
}
