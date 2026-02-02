package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Data
@Validated
@RestController
@RequestMapping("/public")
public class BookController {

    private final BookService bookService;

    @GetMapping(path = "/{chapterUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBookUuid(@PathVariable("chapterUUID") UUID chapterUUID) {
        return bookService.checkChapterInventory(chapterUUID);
    }
}
