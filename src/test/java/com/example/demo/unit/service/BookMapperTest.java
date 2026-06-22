package com.example.demo.unit.service;

import com.example.demo.dto.BookSummary;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookMapperTest {

    private final BookMapper mapper = new BookMapper();

    @Test
    @DisplayName("Should map Book to BookSummary")
    void shouldMapBookToBookSummary() {
        Book book = Instancio.create(Book.class);
        BookSummary result = mapper.toBookSummaryDTO(book);
        assertNotNull(result);
        assertEquals(book.getBookUuID(), result.book_uuid());
        assertEquals(book.getChapterUuID(), result.chapter_uuid());
        assertEquals(book.isCurrentlyBorrowed(), result.currently_borrowed());
    }
}