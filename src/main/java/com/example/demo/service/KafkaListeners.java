package com.example.demo.service;

import com.example.demo.dto.BookPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private final BookService bookService;

    @Autowired
    public KafkaListeners(BookService bookService) {
        this.bookService = bookService;
    }

    @KafkaListener(topics = "borrow_topic", groupId = "groupId")
    void listenerBorrow(BookPayload message) {
        bookService.listenerBorrowBooks(message, true);
    }

    @KafkaListener(topics = "return_topic", groupId = "groupId")
    void listenerReturn(BookPayload message) {
        bookService.listenerBorrowBooks(message, false);
    }
}
