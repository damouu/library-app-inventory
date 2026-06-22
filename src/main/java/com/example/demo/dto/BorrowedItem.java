package com.example.demo.dto;

import java.util.UUID;


public record BorrowedItem(
        UUID book_uuid,

        UUID chapter_uuid
) {
}