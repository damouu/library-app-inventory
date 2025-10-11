package com.example.demo.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookStudentRepository extends JpaRepository<BookMemberCard, Integer>, JpaSpecificationExecutor<BookMemberCard> {

    @Query("SELECT b FROM BookMemberCard b WHERE b.book.id = :id")
    Optional<BookMemberCard> findBookStudentByID(int id);


    @Query("SELECT b FROM BookMemberCard b WHERE b.book.id = :id and b.granted_borrow_extend = false and b.borrow_return_date = null ")
    Optional<BookMemberCard> findBookStudentByIDUpdate(int id);

}
