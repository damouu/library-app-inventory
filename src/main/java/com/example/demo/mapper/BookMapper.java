package com.example.demo.mapper;

import com.example.demo.dto.BookSummary;
import com.example.demo.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookSummary toBookSummaryDTO(Book book) {
        return new BookSummary(book.getBookUuid(), book.getChapterUuid(), book.isCurrentlyBorrowed());
    }

}
