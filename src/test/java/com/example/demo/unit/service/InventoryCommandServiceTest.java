package com.example.demo.unit.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.InventoryCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
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
    @DisplayName("should reserve book and return summary when book is available")
    void reserveBook_shouldReserveAndReturnSummary_whenBookIsAvailable() {
        // Arrange
        UUID bookUuid = UUID.randomUUID();
        UUID chapterUuid = UUID.randomUUID();
        BorrowedItem item = new BorrowedItem(bookUuid, UUID.randomUUID()); // Ton DTO/Record
        List<BorrowedItem> items = List.of(item);
        UUID eventId = UUID.randomUUID();

        Book book = Book.builder().bookUuid(bookUuid).currentlyBorrowed(false).build();

        BookSummary expectedSummary = new BookSummary(bookUuid, chapterUuid, true);

        when(bookRepository.existsByLastBorrowEventId(eventId)).thenReturn(false);
        // Important : On mock l'Optional retourné par le repository
        when(bookRepository.findByBookUuid(bookUuid)).thenReturn(Optional.of(book));
        when(bookMapper.toBookSummaryDTO(book)).thenReturn(expectedSummary);

        // Act
        List<BookSummary> result = inventoryCommandService.reserveBook(items, eventId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedSummary, result.get(0));

        // Vérification de la mutation de l'entité
        assertTrue(book.isCurrentlyBorrowed());
        assertEquals(eventId, book.getLastBorrowEventId());

        // Vérification des appels
        verify(bookRepository).existsByLastBorrowEventId(eventId);
        verify(bookRepository).findByBookUuid(bookUuid); // On vérifie l'appel par UUID de livre
    }

    @Test
    @DisplayName("should return empty list and never query database when event is already processed")
    void reserveBook_shouldReturnEmptyList_whenEventAlreadyProcessed_Idempotency() {
        // Arrange
        BorrowedItem item = new BorrowedItem(UUID.randomUUID(), UUID.randomUUID());
        UUID eventId = UUID.randomUUID();

        when(bookRepository.existsByLastBorrowEventId(eventId)).thenReturn(true);

        // Act
        List<BookSummary> result = inventoryCommandService.reserveBook(List.of(item), eventId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Sécurité : On s'assure qu'on ne cherche aucun livre
        verify(bookRepository, never()).findByBookUuid(any());
    }

    @Test
    @DisplayName("should throw exception when book does not exist or is unavailable")
    void reserveBook_shouldThrowNoAvailableBookException_whenBookNotFound() {
        UUID bookUuid = UUID.randomUUID();
        BorrowedItem item = new BorrowedItem(bookUuid, UUID.randomUUID());
        UUID eventId = UUID.randomUUID();
        when(bookRepository.existsByLastBorrowEventId(eventId)).thenReturn(false);
        when(bookRepository.findByBookUuid(bookUuid)).thenReturn(Optional.empty());
        assertThrows(ChapterNotFoundException.class, () ->
                inventoryCommandService.reserveBook(List.of(item), eventId)
        );
        verify(bookRepository).findByBookUuid(bookUuid);
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