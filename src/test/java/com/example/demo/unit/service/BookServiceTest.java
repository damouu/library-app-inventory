package com.example.demo.unit.service;

import com.example.demo.dto.BorrowEventPayload;
import com.example.demo.dto.ReturnEventPayload;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    Book book;

    BorrowEventPayload borrowEventPayload;

    ReturnEventPayload returnEventPayload;

    @BeforeEach
    void setUp() {
        borrowEventPayload = Instancio.create(BorrowEventPayload.class);
        returnEventPayload = Instancio.create(ReturnEventPayload.class);
        book = Instancio.of(Book.class).ignore(field(Book::getId)).ignore(field(Book::getDeletedDate)).ignore(field(Book::getAddedDate)).create();
    }

    @Test
    void checkChapterInventory() {
        UUID uuid = UUID.randomUUID();
        book.setBookUuID(uuid);
        when(bookRepository.findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(uuid)).thenReturn(Optional.ofNullable(book));
        ResponseEntity<Book> response = bookService.checkChapterInventory(uuid);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getBookUuID(), book.getBookUuID());
        verify(bookRepository, Mockito.times(1)).findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(uuid);
    }

    @Test
    void checkChapterInventory_exception() {
        UUID uuid = UUID.randomUUID();
        book.setBookUuID(uuid);
        when(bookRepository.findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(uuid)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            bookService.checkChapterInventory(uuid);
        });
        verify(bookRepository, Mockito.times(1)).findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(uuid);
    }

    @Test
    void listenerBorrowBooks() {
        bookService.listenerBorrowBooks(borrowEventPayload, true);
        verify(bookRepository, Mockito.times(1)).updateBorrowedStatusInBatch(anyList(), eq(true));
    }

    @Test
    void listenerReturnBorrowedBooks() {
        bookService.listenerReturnBorrowedBooks(returnEventPayload, false);
        verify(bookRepository, Mockito.times(1)).updateBorrowedStatusInBatch(anyList(), eq(false));
    }
}