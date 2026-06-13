package com.example.demo.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowCreatedEventData {

    private UUID member_card_uuid;

    private UUID borrow_uuid;

    private String borrow_start_date;

    private String borrow_end_date;

    private List<BorrowedItem> borrowed_items;
}