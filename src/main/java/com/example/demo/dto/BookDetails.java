package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BookDetails {
    private UUID chapter_uuid;
    private String chapter_title;
    private int chapter_number;
    private String chapter_artwork_cover_URL;
}
