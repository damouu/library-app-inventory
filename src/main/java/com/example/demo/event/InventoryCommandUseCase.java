package com.example.demo.event;

import com.example.demo.dto.BookSummary;
import com.example.demo.dto.BorrowedItem;
import com.example.demo.dto.ChapterCreatedEvent;

import java.util.List;
import java.util.UUID;

public interface InventoryCommandUseCase {

    List<BookSummary> reserveBook(List<BorrowedItem> items, UUID eventId);

    void releaseBook(List<UUID> bookUuids);

    void createChapterStock(ChapterCreatedEvent event);
}