package com.example.demo.exception;

import java.util.UUID;

public class ChapterNotFoundException extends RuntimeException {
    public ChapterNotFoundException(UUID chapterUuid) {
        super("Chapter not found with UUID: " + chapterUuid);
    }
}
