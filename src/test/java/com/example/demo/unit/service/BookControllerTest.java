package com.example.demo.unit.service;

import com.example.demo.controller.BookController;
import com.example.demo.dto.BookSummary;
import com.example.demo.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private BookController bookController;

    @Test
    @DisplayName("Should return book details when valid UUID is provided")
    void shouldReturnBookDetailsWhenValidUuidIsProvided() {
        BookSummary bookSummary = new BookSummary(UUID.randomUUID(), UUID.randomUUID(), false);
        ResponseEntity<BookSummary> expectedResponse = ResponseEntity.ok(bookSummary);
        UUID bookUUID = UUID.randomUUID();
        when(inventoryService.checkChapterInventory(bookUUID)).thenReturn(bookSummary);
        BookSummary response = bookController.getBookUuid(bookUUID);
        assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());
        assertEquals(bookSummary, response);
        verify(inventoryService).checkChapterInventory(bookUUID);
    }
}