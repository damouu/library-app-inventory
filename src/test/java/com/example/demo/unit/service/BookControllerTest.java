package com.example.demo.unit.service;

import com.example.demo.controller.BookController;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    @DisplayName("Should return book details when valid UUID is provided")
    void shouldReturnBookDetailsWhenValidUuidIsProvided() {
        Book book = Instancio.create(Book.class);
        ResponseEntity<Book> expectedResponse = ResponseEntity.ok(book);

        UUID bookUUID = UUID.randomUUID();

        when(bookService.checkChapterInventory(bookUUID)).thenReturn(expectedResponse);

        ResponseEntity<Book> response = bookController.getBookUuid(bookUUID);

        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());

        verify(bookService).checkChapterInventory(bookUUID);
    }
}