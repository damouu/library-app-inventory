package com.example.demo.unit.service;

import com.example.demo.dto.BorrowEventPayload;
import com.example.demo.dto.ReturnEventPayload;
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
        BorrowEventPayload borrowEventPayload = Instancio.create(BorrowEventPayload.class);
        kafkaListeners.listenerBorrow(borrowEventPayload);
        verify(bookService).listenerBorrowBooks(borrowEventPayload, true);

    }


    @Test
    @DisplayName("false")
    void testListenerReturnFalse() {
        ReturnEventPayload returnEventPayload = Instancio.create(ReturnEventPayload.class);
        kafkaListeners.listenerReturn(returnEventPayload);
        verify(bookService).listenerReturnBorrowedBooks(returnEventPayload, false);

    }
}