package com.example.demo.unit.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnCreatedEvent;
import com.example.demo.event.InventoryCommandUseCase;
import com.example.demo.kafka.InventoryKafkaConsumer;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InventoryKafkaConsumerTest {

    @Mock
    private InventoryCommandUseCase commandUseCase;

    private InventoryKafkaConsumer inventoryKafkaConsumer;

    @BeforeEach
    void setup() {
        inventoryKafkaConsumer = new InventoryKafkaConsumer(commandUseCase);
    }

    @Test
    @DisplayName("should delegate borrow event to command use case")
    void testListenerBorrow() {
        BorrowCreatedEvent event = Instancio.create(BorrowCreatedEvent.class);
        inventoryKafkaConsumer.listenerBorrow(event);
        verify(commandUseCase).reserveBook(event.data().borrowed_items(), event.metadata().event_uuid());
    }


    @Test
    @DisplayName("should delegate return event to command use case")
    void testListenerReturn() {
        ReturnCreatedEvent event = Instancio.create(ReturnCreatedEvent.class);
        List<UUID> expectedBookUuids = event.data().returned_items().stream().map(bookToDecrement -> bookToDecrement.book_uuid()).collect(Collectors.toList());
        inventoryKafkaConsumer.listenerReturn(event);
        verify(commandUseCase, times(1)).releaseBook(expectedBookUuids);
    }

    @Test
    @DisplayName("should delegate catalog event to command use case")
    void testListenerCatalog() {
        ChapterCreatedEvent event = Instancio.create(ChapterCreatedEvent.class);
        inventoryKafkaConsumer.listenerCatalog(event);
        verify(commandUseCase).createChapterStock(event);
    }
}