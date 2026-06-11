package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowEventPayload {

    private Metadata metadata;

    private EventData data;

}