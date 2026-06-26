package com.example.demo.unit.service;

import com.example.demo.dto.BookSummary;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ChapterCreatedEventData;
import com.example.demo.dto.Metadata;
import com.example.demo.exception.NoAvailableBookException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.InventoryCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryCommandServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private InventoryCommandService inventoryCommandService;

    @Test
    void reserveBook_shouldReserveAndReturnSummary_whenBookIsAvailable() {
        UUID chapterUuid = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Book book = Book.builder().bookUuid(UUID.randomUUID()).currentlyBorrowed(false).build();
        BookSummary expectedSummary = new BookSummary(chapterUuid, book.getBookUuid(), true);
        when(bookRepository.existsByLastBorrowEventId(eventId)).thenReturn(false);
        when(bookRepository.findAvailableForUpdate(chapterUuid)).thenReturn(List.of(book));
        when(bookMapper.toBookSummaryDTO(book)).thenReturn(expectedSummary);
        BookSummary result = inventoryCommandService.reserveBook(chapterUuid, eventId);
        assertNotNull(result);
        assertTrue(book.isCurrentlyBorrowed());
        assertEquals(eventId, book.getLastBorrowEventId());
    }

    @Test
    void reserveBook_shouldReturnNull_whenEventAlreadyProcessed_Idempotency() {
        UUID chapterUuid = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        when(bookRepository.existsByLastBorrowEventId(eventId)).thenReturn(true);
        BookSummary result = inventoryCommandService.reserveBook(chapterUuid, eventId);
        assertNull(result);
        verify(bookRepository, never()).findAvailableForUpdate(any());
    }

    @Test
    void reserveBook_shouldThrowNoAvailableBookException_whenNoBookStockLeft() {
        UUID chapterUuid = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        when(bookRepository.existsByLastBorrowEventId(eventId)).thenReturn(false);
        when(bookRepository.findAvailableForUpdate(chapterUuid)).thenReturn(Collections.emptyList());
        assertThrows(NoAvailableBookException.class, () -> inventoryCommandService.reserveBook(chapterUuid, eventId));
    }

    @Test
    void releaseBook_shouldCallRepositoryRelease() {
        List<UUID> uuids = List.of(UUID.randomUUID(), UUID.randomUUID());
        inventoryCommandService.releaseBook(uuids);
        verify(bookRepository, times(1)).releaseBooks(uuids);
    }

    @Test
    void createChapterStock_shouldCreateRequestedNumberOfCopies() {
        UUID chapterUuid = UUID.randomUUID();
        UUID eventUuid = UUID.randomUUID();
        Metadata metadata = new Metadata("2026-06-26T12:00:00Z", "catalog-service", "CHAPTER_CREATED", eventUuid);
        ChapterCreatedEventData data = new ChapterCreatedEventData(chapterUuid, UUID.randomUUID(), "Naruto", "Uzumaki Naruto", 20, 1, "The beginning of the ninja journey.", "http://example.com/cover.jpg", "1999-09-21", 3 // On teste la création de 3 copies
        );
        ChapterCreatedEvent event = new ChapterCreatedEvent(metadata, data);
        inventoryCommandService.createChapterStock(event);
        @SuppressWarnings("unchecked") ArgumentCaptor<List<Book>> booksCaptor = ArgumentCaptor.forClass(List.class);
        verify(bookRepository, times(1)).saveAll(booksCaptor.capture());
        List<Book> savedBooks = booksCaptor.getValue();
        assertEquals(3, savedBooks.size());
        savedBooks.forEach(book -> {
            assertEquals(chapterUuid, book.getChapterUuid());
            assertFalse(book.isCurrentlyBorrowed());
            assertNotNull(book.getBookUuid());
        });
    }
}