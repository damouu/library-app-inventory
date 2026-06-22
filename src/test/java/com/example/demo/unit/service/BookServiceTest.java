package com.example.demo.unit.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    Book book;

    BorrowEventPayload borrowEventPayload;

    ReturnCreatedEvent returnCreatedEvent;

    BorrowCreatedEvent borrowCreatedEvent;

    @BeforeEach
    void setUp() {
        borrowEventPayload = Instancio.create(BorrowEventPayload.class);
        returnCreatedEvent = Instancio.create(ReturnCreatedEvent.class);
        borrowCreatedEvent = Instancio.create(BorrowCreatedEvent.class);
        book = Instancio.of(Book.class).ignore(field(Book::getId)).ignore(field(Book::getDeletedDate)).ignore(field(Book::getAddedDate)).create();
    }

    @Test
    void checkChapterInventory() {
        UUID uuid = UUID.randomUUID();
        BookSummary bookSummary = new BookSummary(uuid, uuid, false);
        book.setBookUuID(uuid);
        when(bookMapper.toBookSummaryDTO(book)).thenReturn(bookSummary);
        when(bookRepository.findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(uuid)).thenReturn(Optional.ofNullable(book));
        BookSummary inventory = bookService.checkChapterInventory(uuid);
        Assertions.assertFalse(inventory.currently_borrowed());
        verify(bookRepository, times(1)).findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(uuid);
        verify(bookMapper, times(1)).toBookSummaryDTO(book);
    }

    @Test
    void checkChapterInventory_exception() {
        UUID uuid = UUID.randomUUID();
        book.setBookUuID(uuid);
        when(bookRepository.findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(uuid)).thenReturn(Optional.empty());
        Assertions.assertThrows(ChapterNotFoundException.class, () -> {
            bookService.checkChapterInventory(uuid);
        });
        verify(bookRepository, times(1)).findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(uuid);
    }

    @Test
    void listenerBorrowBooks() {
        bookService.listenerBorrowBooks(borrowCreatedEvent, true);
        verify(bookRepository, times(1)).updateBorrowedStatusInBatch(anyList(), eq(true));
    }

    @Test
    void listenerReturnBorrowedBooks() {
        bookService.listenerReturnBorrowedBooks(returnCreatedEvent, false);
        verify(bookRepository, times(1)).updateBorrowedStatusInBatch(anyList(), eq(false));
    }


    @Test
    void listenerCatalogBooks_shouldCreateRequestedNumberOfCopies() {
        UUID chapterUuid = UUID.randomUUID();
        UUID eventUuid = UUID.randomUUID();
        Metadata metadata = new Metadata("dede", eventUuid, "dede", "dede", eventUuid);
        ChapterCreatedEventData data2 = new ChapterCreatedEventData(chapterUuid, UUID.randomUUID(), "dede", "dede", 10, 1, "dede", "dede", "dede", 1);
        ChapterCreatedEvent event2 = new ChapterCreatedEvent(metadata, data2);
        bookService.listenerCatalogBooks(event2);
        ArgumentCaptor<List<Book>> booksCaptor = ArgumentCaptor.forClass(List.class);
        verify(bookRepository, times(1)).saveAll(booksCaptor.capture());
        List<Book> savedBooks = booksCaptor.getValue();
        Assertions.assertEquals(1, savedBooks.size());
        savedBooks.forEach(book -> {
            Assertions.assertEquals(chapterUuid, book.getChapterUuID());
            Assertions.assertFalse(book.isCurrentlyBorrowed());
            Assertions.assertNotNull(book.getBookUuID());
        });
    }

}