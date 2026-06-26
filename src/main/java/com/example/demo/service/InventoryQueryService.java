package com.example.demo.service;

import com.example.demo.dto.BookSummary;
import com.example.demo.event.InventoryQueryUseCase;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryQueryService implements InventoryQueryUseCase {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookSummary checkChapterInventory(UUID chapterUuid) {
        Book book = bookRepository.findFirstByChapterUuidAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(chapterUuid).orElseThrow(() -> new ChapterNotFoundException(chapterUuid));
        return bookMapper.toBookSummaryDTO(book);
    }
}