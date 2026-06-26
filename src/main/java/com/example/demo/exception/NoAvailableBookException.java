package com.example.demo.exception;

import java.util.UUID;

public class NoAvailableBookException extends RuntimeException {
    public NoAvailableBookException(UUID chapterUuid) {
        super("no book available for chapter UUID: " + chapterUuid);
    }
}
