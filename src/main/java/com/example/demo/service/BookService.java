package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * find a book by the passed UUID.
     *
     * @param chapterUuid a book's UUID
     * @return Array returns if the given UUID exists, a book resource and also attach to it, the studentCard associated to it.
     * @throws ResponseStatusException throws an exception if the given UUID does not correspond to a book in the database.
     */
    public ResponseEntity<Book> checkChapterInventory(UUID chapterUuid) throws ResponseStatusException {
        Book book = bookRepository.findFirstBychapterUuIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(chapterUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
        return ResponseEntity.ok(book);
    }


    /**
     * Listener borrow books.
     *
     * @param borrowPayloadData the borrowed data
     */
    @Transactional
    public void listenerBorrowBooks(BorrowCreatedEvent borrowCreatedEvent, Boolean isBorrowed) {
        List<UUID> booksUuidToBorrow = borrowCreatedEvent.getData().getBorrowed_items().stream().map(BorrowedItem::getBook_uuid).toList();
        bookRepository.updateBorrowedStatusInBatch(booksUuidToBorrow, isBorrowed);
    }

    /**
     * Listener borrow books.
     *
     * @param returnEventPayload the borrowed data
     */
    @Transactional
    public void listenerReturnBorrowedBooks(ReturnEventPayload returnEventPayload, Boolean isBorrowed) {
        List<UUID> booksUuidToBorrow = returnEventPayload.getData().getInventoryData().getBooks().stream().map(BookToDecrement::getBook_uuid).toList();
        bookRepository.updateBorrowedStatusInBatch(booksUuidToBorrow, isBorrowed);
    }

    /**
     * @param chapterCreatedEvent
     */
    @Transactional
    public void listenerCatalogBooks(ChapterCreatedEvent chapterCreatedEvent) {
        List<Book> copies = new ArrayList<>();

        for (int i = 0; i < chapterCreatedEvent.getData().getInitial_copies_count(); i++) {
            copies.add(Book.builder().bookUuID(UUID.randomUUID()).chapterUuID(chapterCreatedEvent.getData().getChapter_uuid()).currentlyBorrowed(false).build());

        }
        bookRepository.saveAll(copies);
    }

}
