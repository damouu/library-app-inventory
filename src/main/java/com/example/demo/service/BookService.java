package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * The type Book service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    /**
     * find a book by the passed UUID.
     *
     * @param chapterUuid a book's UUID
     * @return Array returns if the given UUID exists, a book resource and also attach to it, the studentCard associated to it.
     * @throws ChapterNotFoundException throws an exception if the given UUID does not correspond to a book in the database.
     */
    public BookSummary checkChapterInventory(UUID chapterUuid) throws ChapterNotFoundException {
        Book book = bookRepository.findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(chapterUuid).orElseThrow(() -> new ChapterNotFoundException(chapterUuid));
        return bookMapper.toBookSummaryDTO(book);
    }


    /**
     * Listener borrow books.
     *
     * @param event      the borrowed data
     * @param isBorrowed the borrowed data
     */
    @Transactional
    public void listenerBorrowBooks(BorrowCreatedEvent event, Boolean isBorrowed) {
        List<UUID> booksUuidToBorrow = event.data().borrowed_items().stream().map(BorrowedItem::book_uuid).toList();
        log.info("Processing BORROW_CREATED event {} for books {}", event.metadata().event_uuid(), booksUuidToBorrow);
        bookRepository.updateBorrowedStatusInBatch(booksUuidToBorrow, isBorrowed);
        log.info("Processed BORROW_CREATED event {} for books {}", event.metadata().event_uuid(), booksUuidToBorrow);
    }

    /**
     * Listener return books.
     *
     * @param event      the borrowed data
     * @param isBorrowed the borrowed data
     */
    @Transactional
    public void listenerReturnBorrowedBooks(ReturnCreatedEvent event, Boolean isBorrowed) {
        List<UUID> returnedBookUuids = event.data().returned_items().stream().map(BookToDecrement::book_uuid).toList();
        log.info("Processing BORROW_RETURN event {} for books {}", event.metadata().event_uuid(), returnedBookUuids);
        bookRepository.updateBorrowedStatusInBatch(returnedBookUuids, isBorrowed);
        log.info("Processed BORROW_RETURN event {} for books {}", event.metadata().event_uuid(), returnedBookUuids);
    }

    /**
     * Listener catalog books.
     *
     * @param event dede
     */
    @Transactional
    public void listenerCatalogBooks(ChapterCreatedEvent event) {
        List<Book> copies = new ArrayList<>();
        log.info("Processing CHAPTER_CREATED event {} for chapter {}", event.metadata().event_uuid(), event.data().chapter_uuid());
        for (int i = 0; i < event.data().initial_copies_count(); i++) {
            copies.add(Book.builder().bookUuID(UUID.randomUUID()).chapterUuID(event.data().chapter_uuid()).currentlyBorrowed(false).build());
        }
        bookRepository.saveAll(copies);
        log.info("Processed CHAPTER_CREATED event {} for chapter {}, created {} books", event.metadata().event_uuid(), event.data().chapter_uuid(), copies.size());
    }

}
