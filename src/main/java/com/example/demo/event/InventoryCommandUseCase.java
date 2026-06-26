package com.example.demo.event;

import com.example.demo.dto.BookSummary;
import com.example.demo.dto.ChapterCreatedEvent;

import java.util.List;
import java.util.UUID;

public interface InventoryCommandUseCase {

    BookSummary reserveBook(UUID chapterUuid, UUID eventId);

    void releaseBook(List<UUID> bookUuids);

    void createChapterStock(ChapterCreatedEvent event);
}