package com.example.demo.unit.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnCreatedEvent;
import com.example.demo.service.InventoryService;
import com.example.demo.kafka.InventoryKafkaConsumer;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InventoryKafkaConsumerTest {

    @Mock
    private InventoryService inventoryService;

    private InventoryKafkaConsumer inventoryKafkaConsumer;


    @BeforeEach
    void setup() {
        inventoryKafkaConsumer = new InventoryKafkaConsumer(inventoryService);
    }

    @Test
    @DisplayName("true")
    void testListenerBorrowFalse() {
        BorrowCreatedEvent borrowCreatedEvent = Instancio.create(BorrowCreatedEvent.class);
        inventoryKafkaConsumer.listenerBorrow(borrowCreatedEvent);
        verify(inventoryService).handleBorrow(borrowCreatedEvent, true);

    }


    @Test
    @DisplayName("false")
    void testListenerReturnFalse() {
        ReturnCreatedEvent returnCreatedEvent = Instancio.create(ReturnCreatedEvent.class);
        inventoryKafkaConsumer.listenerReturn(returnCreatedEvent);
        verify(inventoryService).handleReturn(returnCreatedEvent, false);

    }

    @Test
    @DisplayName("testListenerCatalog")
    void testListenerCatalog() {
        ChapterCreatedEvent chapterCreatedEvent = Instancio.create(ChapterCreatedEvent.class);
        inventoryKafkaConsumer.listenerCatalog(chapterCreatedEvent);
        verify(inventoryService).handleChapterCreated(chapterCreatedEvent);

    }
}