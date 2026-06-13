package com.example.demo.unit.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnCreatedEvent;
import com.example.demo.service.BookService;
import com.example.demo.service.KafkaListeners;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaListenersTest {

    @Mock
    private BookService bookService;

    private KafkaListeners kafkaListeners;


    @BeforeEach
    void setup() {
        kafkaListeners = new KafkaListeners(bookService);
    }

    @Test
    @DisplayName("true")
    void testListenerBorrowFalse() {
        BorrowCreatedEvent borrowCreatedEvent = Instancio.create(BorrowCreatedEvent.class);
        kafkaListeners.listenerBorrow(borrowCreatedEvent);
        verify(bookService).listenerBorrowBooks(borrowCreatedEvent, true);

    }


    @Test
    @DisplayName("false")
    void testListenerReturnFalse() {
        ReturnCreatedEvent returnCreatedEvent = Instancio.create(ReturnCreatedEvent.class);
        kafkaListeners.listenerReturn(returnCreatedEvent);
        verify(bookService).listenerReturnBorrowedBooks(returnCreatedEvent, false);

    }

    @Test
    @DisplayName("testListenerCatalog")
    void testListenerCatalog() {
        ChapterCreatedEvent chapterCreatedEvent = Instancio.create(ChapterCreatedEvent.class);
        kafkaListeners.listenerCatalog(chapterCreatedEvent);
        verify(bookService).listenerCatalogBooks(chapterCreatedEvent);

    }
}