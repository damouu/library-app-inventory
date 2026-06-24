package com.example.demo.controller;

import com.example.demo.dto.BookSummary;
import com.example.demo.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class BookController {

    private final InventoryService inventoryService;

    @GetMapping(path = "/{chapterUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookSummary getBookUuid(@PathVariable("chapterUUID") UUID chapterUUID) {
        return inventoryService.checkChapterInventory(chapterUUID);
    }
}
