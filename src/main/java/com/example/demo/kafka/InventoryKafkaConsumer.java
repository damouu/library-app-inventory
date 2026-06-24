package com.example.demo.kafka;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnCreatedEvent;
import com.example.demo.event.InventoryUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * The type Kafka listeners.
 */
@Component
public class InventoryKafkaConsumer {


    private final InventoryUseCase inventoryUseCase;

    /**
     * Instantiates a new Kafka listeners.
     *
     * @param inventoryUseCase the book service
     */
    @Autowired
    public InventoryKafkaConsumer(InventoryUseCase inventoryUseCase) {
        this.inventoryUseCase = inventoryUseCase;
    }

    /**
     * Listener borrow.
     *
     * @param borrowCreatedEvent the borrow created event
     */
    @KafkaListener(topics = "library.borrow.v1", groupId = "inventory-group", containerFactory = "factory")
    public void listenerBorrow(@Payload BorrowCreatedEvent borrowCreatedEvent) {
        inventoryUseCase.handleBorrow(borrowCreatedEvent, true);
    }

    /**
     * Listener return.
     *
     * @param returnCreatedEvent the return created event
     */
    @KafkaListener(topics = "library.return.v1", groupId = "inventory-group", containerFactory = "factory")
    public void listenerReturn(@Payload ReturnCreatedEvent returnCreatedEvent) {
        inventoryUseCase.handleReturn(returnCreatedEvent, false);
    }

    /**
     * Listener catalog.
     *
     * @param chapterCreatedEvent the chapter created event
     */
    @KafkaListener(topics = "library.catalog.v1", groupId = "inventory-group", containerFactory = "factory")
    public void listenerCatalog(@Payload ChapterCreatedEvent chapterCreatedEvent) {
        inventoryUseCase.handleChapterCreated(chapterCreatedEvent);
    }
}