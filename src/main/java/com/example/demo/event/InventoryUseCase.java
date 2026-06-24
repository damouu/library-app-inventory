package com.example.demo.event;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnCreatedEvent;

public interface InventoryUseCase {

    void handleBorrow(BorrowCreatedEvent event, boolean isBorrow);

    void handleReturn(ReturnCreatedEvent event, boolean isBorrow);

    void handleChapterCreated(ChapterCreatedEvent event);
}