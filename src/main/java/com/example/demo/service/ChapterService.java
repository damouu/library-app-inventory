package com.example.demo.service;

import com.example.demo.model.Chapter;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.spec.ChapterSpecification;
import com.example.demo.util.PaginationUtil;
import com.example.demo.view.BorrowPopularView;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * The type Chapter service.
 */
@Service
@Data
public class ChapterService {

    private final BookRepository bookRepository;

    private final ChapterRepository chapterRepository;


    public ResponseEntity<?> getChapters(Map allParams) {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
        HashMap<Integer, Object> dede = new LinkedHashMap<>();
        Pageable pageable = PaginationUtil.extractPage(allParams);
        String typeValue = Optional.ofNullable(allParams.get("type")).map(Object::toString).orElse(null);
        switch (typeValue) {
            case ("recent"):
                Specification<Chapter> specification = ChapterSpecification.publishedBetween(startOfWeek, endOfWeek);
                Page<Chapter> chapters = chapterRepository.findAll(specification, pageable);
                return ResponseEntity.status(HttpStatus.OK).body(chapters);
            case ("popular"):
                int count = 0;
                final Page<BorrowPopularView> chapters2 = chapterRepository.getChaptersByMostBorrowsByDate(startOfWeek, endOfWeek, pageable);
                for (BorrowPopularView chapter : chapters2) {
                    HashMap<String, String> data = new LinkedHashMap<>();
                    data.put("chapter_title", chapter.getTitle());
                    data.put("chapter_second_title", chapter.getSecondTitle());
                    data.put("chapter_number", chapter.getChapterNumber().toString());
                    data.put("chapter_uuid", chapter.getChapterUuid());
                    data.put("borrow_number", chapter.getBorrowCount().toString());
                    dede.put(count, data);
                    count++;
                }
                return ResponseEntity.status(HttpStatus.OK).body(dede);
            case null:
                specification = ChapterSpecification.filterChapter(allParams);
                chapters = chapterRepository.findAll(specification, pageable);
                return ResponseEntity.status(HttpStatus.OK).body(chapters);
            default:
                throw new IllegalStateException("Unexpected value: " + typeValue);
        }
    }


    public Chapter getChapterUUID(UUID chapterUUID) throws ResponseStatusException {
        final Optional<Chapter> chapter = Optional.ofNullable(chapterRepository.getChaptersByChapter_uuid(chapterUUID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "chapter does not exist")));
        chapter.get().setBooks(bookRepository.findRecentBooksByChapter(chapter.get(), PageRequest.of(0, 1)));
        return chapter.get();
    }
}
