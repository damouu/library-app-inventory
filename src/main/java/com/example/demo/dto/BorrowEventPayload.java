package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowEventPayload {

    private EventData data;

    private Metadata metadata;

}