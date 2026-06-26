package com.example.demo.unit.controller; // Correction du package (optionnelle mais recommandée)

import com.example.demo.controller.BookController;
import com.example.demo.dto.BookSummary;
import com.example.demo.service.InventoryQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private InventoryQueryService inventoryQueryService;

    @InjectMocks
    private BookController bookController;

    @Test
    @DisplayName("Should return book details when valid UUID is provided")
    void shouldReturnBookDetailsWhenValidUuidIsProvided() {
        UUID chapterUuid = UUID.randomUUID();
        BookSummary expectedSummary = new BookSummary(chapterUuid, UUID.randomUUID(), false);
        when(inventoryQueryService.checkChapterInventory(chapterUuid)).thenReturn(expectedSummary);
        BookSummary actualResponse = bookController.getbookUuid(chapterUuid);
        assertEquals(expectedSummary, actualResponse);
        verify(inventoryQueryService).checkChapterInventory(chapterUuid);
    }

}