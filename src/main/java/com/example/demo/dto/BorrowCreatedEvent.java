package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowCreatedEvent {

    private Metadata metadata;

    private BorrowCreatedEventData data;
}
