package com.example.demo.dto;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    private String timestamp;
    private UUID memberCardUUID;
    private String source_service;
    private String event_type;
    private UUID event_uuid;
}