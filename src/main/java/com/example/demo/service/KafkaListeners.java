package com.example.demo.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * The type Kafka listeners.
 */
@Component
public class KafkaListeners {

    private final BookService bookService;

    /**
     * Instantiates a new Kafka listeners.
     *
     * @param bookService the book service
     */
    @Autowired
    public KafkaListeners(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Listener borrow.
     *
     * @param borrowCreatedEvent the borrow created event
     */
    @KafkaListener(topics = "library.borrow.v1", groupId = "inventory-group", containerFactory = "factory")
    public void listenerBorrow(@Payload BorrowCreatedEvent borrowCreatedEvent) {
        bookService.listenerBorrowBooks(borrowCreatedEvent, true);
    }

    /**
     * Listener return.
     *
     * @param returnCreatedEvent the return created event
     */
    @KafkaListener(topics = "library.return.v1", groupId = "inventory-group", containerFactory = "factory")
    public void listenerReturn(@Payload ReturnCreatedEvent returnCreatedEvent) {
        bookService.listenerReturnBorrowedBooks(returnCreatedEvent, false);
    }

    /**
     * Listener catalog.
     *
     * @param chapterCreatedEvent the chapter created event
     */
    @KafkaListener(topics = "library.catalog.v1", groupId = "inventory-group", containerFactory = "factory")
    public void listenerCatalog(@Payload ChapterCreatedEvent chapterCreatedEvent) {
        bookService.listenerCatalogBooks(chapterCreatedEvent);
    }
}