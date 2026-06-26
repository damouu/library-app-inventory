package com.example.demo.controller;

import com.example.demo.dto.BookSummary;
import com.example.demo.service.InventoryQueryService;
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

    private final InventoryQueryService inventoryQueryService;

    @GetMapping(path = "/{chapterUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookSummary getbookUuid(@PathVariable("chapterUuid") UUID chapterUuid) {
        return inventoryQueryService.checkChapterInventory(chapterUuid);
    }
}
