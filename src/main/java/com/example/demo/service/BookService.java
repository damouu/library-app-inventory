package com.example.demo.service;

import com.example.demo.dto.BookToDecrement;
import com.example.demo.dto.BorrowEventPayload;
import com.example.demo.dto.ReturnEventPayload;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@Data
@Service
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
        Book book = bookRepository.findFirstByChapterUUIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(chapterUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
        return ResponseEntity.ok(book);
    }


    /**
     * Listener borrow books.
     *
     * @param borrowPayloadData the borrowed data
     */
    @Transactional
    public void listenerBorrowBooks(BorrowEventPayload borrowPayloadData, Boolean isBorrowed) {
        List<UUID> booksUuidToBorrow = borrowPayloadData.getData().getInventoryData().getBooks().stream().map(BookToDecrement::getBook_uuid).toList();
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

}
