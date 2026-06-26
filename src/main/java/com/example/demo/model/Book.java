package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book", indexes = {@Index(name = "idx_book_chapter", columnList = "chapter_uuid"), @Index(name = "idx_book_uuid", columnList = "book_uuid", unique = true)})
public class Book {

    @Id
    @SequenceGenerator(name = "book_sequence", allocationSize = 1, sequenceName = "book_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence")
    @Column(updatable = false, nullable = false)
    @Getter(onMethod = @__(@JsonIgnore))
    private Integer id;

    @Column(nullable = false, updatable = false, unique = true, name = "book_uuid")
    private UUID bookUuid;

    @Column(nullable = false, updatable = false, name = "chapter_uuid")
    private UUID chapterUuid;

    @Column(nullable = false)
    private boolean currentlyBorrowed;

    @Column(unique = true)
    private UUID lastBorrowEventId;

    @Column(nullable = false, updatable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter(onMethod = @__(@JsonIgnore))
    @JsonIgnore
    @CreationTimestamp
    private LocalDate addedDate;

    @Column(columnDefinition = "timestamp")
    private LocalDate deletedDate;

}
