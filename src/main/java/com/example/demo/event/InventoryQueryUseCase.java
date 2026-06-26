package com.example.demo.event;

import com.example.demo.dto.BookSummary;

import java.util.UUID;

public interface InventoryQueryUseCase {

    BookSummary checkChapterInventory(UUID chapterUuid);
}