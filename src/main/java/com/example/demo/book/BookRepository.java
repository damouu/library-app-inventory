package com.example.demo.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query("SELECT b FROM book b WHERE b.deleted_at is null and b.uuid = :uuid")
    Optional<Book> findByUuid(UUID uuid);

    Optional<List<Book>> findByTitleContaining(@Param("title") String title, Pageable pageable);

    Optional<Collection<Book>> findAllByTotalPages(Optional<Integer> integer);

    Optional<Collection<Book>> findAllByGenre(Optional<String> stringOptional);

    Optional<Collection<Book>> findAllByGenreAndTotalPages(Optional<String> genre, Optional<Integer> totalPages);

    Page<Book> findAll(Specification<Book> specification, Pageable pageable);

    @Query("SELECT b FROM book b WHERE b.deleted_at is null and b.uuid = :uuid")
    Optional<BookMemberCard> findBookStudentByUUID(UUID uuid);

}
