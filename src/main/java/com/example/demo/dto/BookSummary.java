package com.example.demo.dto;

import java.util.UUID;

public record BookSummary(
        UUID book_uuid,
        UUID chapter_uuid,
        boolean currently_borrowed
) {
}
