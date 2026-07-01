package com.example.demo.kafka;

import com.example.demo.dto.BookToDecrement;
import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnCreatedEvent;
import com.example.demo.event.InventoryCommandUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryKafkaConsumer {

    private final InventoryCommandUseCase commandUseCase;

    @KafkaListener(topics = "library.borrow.v1")
    public void listenerBorrow(BorrowCreatedEvent event) {
        UUID eventId = event.metadata().event_uuid();
        commandUseCase.reserveBook(event.data().borrowed_items(), eventId);
    }

    @KafkaListener(topics = "library.return.v1")
    public void listenerReturn(ReturnCreatedEvent event) {
        List<UUID> bookUuids = event.data().returned_items().stream().map(BookToDecrement::book_uuid).toList();
        commandUseCase.releaseBook(bookUuids);
    }

    @KafkaListener(topics = "library.catalog.v1")
    public void listenerCatalog(ChapterCreatedEvent event) {
        commandUseCase.createChapterStock(event);
    }
}