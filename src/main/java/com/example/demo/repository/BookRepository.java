package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
                SET b.currentlyBorrowed = false,
                    b.lastBorrowEventId = null 
                WHERE b.bookUuid IN :bookUuids
                  AND b.currentlyBorrowed = true
            """)
    void releaseBooks(@Param("bookUuids") List<UUID> bookUuids);
    

    Optional<Book> findByBookUuid(UUID bookUuid);

}