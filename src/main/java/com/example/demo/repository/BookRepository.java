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

    Optional<Book> findFirstByChapterUUIDAndDeletedDateIsNullAndCurrentlyBorrowedIsFalse(UUID chapterUUID);

    Page<Book> findAll(Specification<Book> specification, Pageable pageable);

    @Modifying
    @Query("UPDATE book b SET b.currentlyBorrowed = :status WHERE b.bookUUID IN :bookUuids")
    void updateBorrowedStatusInBatch(@Param("bookUuids") List<UUID> bookUuids, @Param("status") boolean status);

}
