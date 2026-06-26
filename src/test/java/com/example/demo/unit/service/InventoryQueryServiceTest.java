package com.example.demo.unit.service;

import com.example.demo.dto.BookSummary;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.InventoryQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryQueryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private InventoryQueryService inventoryQueryService;

    @Test
    @DisplayName("Should return BookSummary when an available book is found for the chapter")
    void checkChapterInventory_Success() {
        UUID chapterUuid = UUID.randomUUID();
        Book mockBook = new Book();
        BookSummary expectedSummary = new BookSummary(chapterUuid, UUID.randomUUID(), false);
        when(bookRepository.findFirstByChapterUuidAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(chapterUuid)).thenReturn(Optional.of(mockBook));
        when(bookMapper.toBookSummaryDTO(mockBook)).thenReturn(expectedSummary);
        BookSummary actualSummary = inventoryQueryService.checkChapterInventory(chapterUuid);
        assertNotNull(actualSummary, "Le résumé du livre ne devrait pas être null");
        assertEquals(expectedSummary, actualSummary, "Le DTO retourné doit correspondre à celui du mapper");
        verify(bookRepository, times(1)).findFirstByChapterUuidAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(chapterUuid);
        verify(bookMapper, times(1)).toBookSummaryDTO(mockBook);
    }

    @Test
    @DisplayName("Should throw ChapterNotFoundException when no available book is found")
    void checkChapterInventory_ThrowsChapterNotFoundException() {
        UUID chapterUuid = UUID.randomUUID();
        when(bookRepository.findFirstByChapterUuidAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(chapterUuid)).thenReturn(Optional.empty());
        ChapterNotFoundException exception = assertThrows(ChapterNotFoundException.class, () -> {
            inventoryQueryService.checkChapterInventory(chapterUuid);
        });

        verify(bookRepository, times(1)).findFirstByChapterUuidAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(chapterUuid);
        verifyNoInteractions(bookMapper);
    }
}