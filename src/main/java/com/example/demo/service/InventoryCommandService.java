package com.example.demo.service;

import com.example.demo.dto.BookSummary;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.event.InventoryCommandUseCase;
import com.example.demo.exception.NoAvailableBookException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryCommandService implements InventoryCommandUseCase {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public BookSummary reserveBook(UUID chapterUuid, UUID eventId) {
        if (bookRepository.existsByLastBorrowEventId(eventId)) {
            return null;
        }
        Book book = bookRepository.findAvailableForUpdate(chapterUuid).stream().findFirst().orElseThrow(() -> new NoAvailableBookException(chapterUuid));
        book.setCurrentlyBorrowed(true);
        book.setLastBorrowEventId(eventId);
        return bookMapper.toBookSummaryDTO(book);
    }

    @Transactional
    public void releaseBook(List<UUID> bookUuids) {
        bookRepository.releaseBooks(bookUuids);
    }

    @Transactional
    public void createChapterStock(ChapterCreatedEvent event) {
        List<Book> copies = new ArrayList<>();
        log.info("Processing CHAPTER_CREATED event {} for chapter {}", event.metadata().event_uuid(), event.data().chapter_uuid());
        for (int i = 0; i < event.data().initial_copies_count(); i++) {
            copies.add(Book.builder().bookUuid(UUID.randomUUID()).chapterUuid(event.data().chapter_uuid()).currentlyBorrowed(false).build());
        }
        bookRepository.saveAll(copies);
        log.info("Processed CHAPTER_CREATED event {} for chapter {}, created {} books", event.metadata().event_uuid(), event.data().chapter_uuid(), copies.size());
    }
}