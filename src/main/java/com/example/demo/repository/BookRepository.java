package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    Optional<Book> findFirstByChapterUuidAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(UUID chapterUuid);

    Page<Book> findAll(Specification<Book> specification, Pageable pageable);

    boolean existsByLastBorrowEventId(UUID eventId);

    @Modifying
    @Query("""
                UPDATE Book b
                SET b.currentlyBorrowed = false
                WHERE b.bookUuid IN :bookUuids
                  AND b.currentlyBorrowed = true
            """)
    void releaseBooks(@Param("bookUuids") List<UUID> bookUuids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT b FROM Book b
                WHERE b.chapterUuid = :chapterUuid
                  AND b.currentlyBorrowed = false
                  AND b.deletedDate IS NULL
                ORDER BY b.addedDate ASC
            """)
    List<Book> findAvailableForUpdate(UUID chapterUuid);

}