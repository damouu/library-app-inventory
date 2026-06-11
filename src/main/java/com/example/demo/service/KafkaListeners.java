package com.example.demo.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnEventPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private final BookService bookService;

    @Autowired
    public KafkaListeners(BookService bookService) {
        this.bookService = bookService;
    }

    @KafkaListener(topics = "library.borrow.v1", groupId = "inventory-group", containerFactory = "factory")
    public void listenerBorrow(@Payload BorrowCreatedEvent borrowCreatedEvent) {
        bookService.listenerBorrowBooks(borrowCreatedEvent, true);
    }

    @KafkaListener(topics = "library.return.v1", groupId = "inventory-group", containerFactory = "factory")
    public void listenerReturn(@Payload ReturnEventPayload returnEventPayload) {
        bookService.listenerReturnBorrowedBooks(returnEventPayload, false);
    }

    @KafkaListener(topics = "library.catalog.v1", groupId = "inventory-group", containerFactory = "factory")
    public void listenerCatalog(@Payload ChapterCreatedEvent chapterCreatedEvent) {
        bookService.listenerCatalogBooks(chapterCreatedEvent);
    }
}