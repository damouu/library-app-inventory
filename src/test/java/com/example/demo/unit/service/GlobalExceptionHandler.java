package com.example.demo.unit.service;

import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should return 404 when ChapterNotFoundException is thrown")
    void shouldHandleChapterNotFoundException() {
        UUID chapterUuid = UUID.randomUUID();
        ChapterNotFoundException exception = new ChapterNotFoundException(chapterUuid);
        ResponseEntity<String> response = handler.handleException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(exception.getMessage(), response.getBody());
    }
}