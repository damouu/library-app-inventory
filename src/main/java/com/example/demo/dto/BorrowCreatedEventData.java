package com.example.demo.dto;

import java.util.List;
import java.util.UUID;


public record BorrowCreatedEventData(
        UUID member_card_uuid,

        UUID borrow_uuid,

        String borrow_start_date,

        String borrow_end_date,

        List<BorrowedItem> borrowed_items
) {
}