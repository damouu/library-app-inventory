package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnCreatedEvent {

    private Metadata metadata;

    private ReturnCreatedEventData data;

}