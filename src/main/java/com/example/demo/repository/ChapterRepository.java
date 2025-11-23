package com.example.demo.repository;

import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.view.BorrowPopularView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer>, JpaSpecificationExecutor<Chapter> {

    Page<Chapter> findAll(Specification<Chapter> specification, Pageable pageable);

    @Query("SELECT b FROM chapter b WHERE b.chapterUUID = :chapterUUID and b.deleted_at is null")
    Optional<Chapter> getChaptersByChapter_uuid(UUID chapterUUID);


    @Query("SELECT b FROM chapter b WHERE b.series = :series and b.deleted_at is null ")
    Page<Chapter> findSeriesBySeries_uuidAndChapters(@Param("series") Series series, Pageable pageable);


    @Query(value = """
            SELECT c.title AS title,
                               c.second_title AS secondTitle,
                               c.chapter_number AS chapterNumber,
                               CAST(c.chapter_uuid AS VARCHAR) AS chapterUuid,
                               COUNT(bmc.id) AS borrowCount
                        FROM book_member_card bmc
                        JOIN book b ON b.id = bmc.book_id
                        JOIN chapter c ON c.id = b.chapter_id
                        WHERE bmc.borrow_start_date BETWEEN :startWeek AND :endWeek
                        GROUP BY c.title, c.second_title, c.chapter_number, c.chapter_uuid
            """, countQuery = """
            SELECT COUNT(DISTINCT c.id)
            FROM book_member_card bmc
            JOIN book b ON b.id = bmc.book_id
            JOIN chapter c ON c.id = b.chapter_id
            WHERE bmc.borrow_start_date BETWEEN :startWeek AND :endWeek
            """, nativeQuery = true)
    Page<BorrowPopularView> getChaptersByMostBorrowsByDate(LocalDate startWeek, LocalDate endWeek, Pageable pageable);


}
