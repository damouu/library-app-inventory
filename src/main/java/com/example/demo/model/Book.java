package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "book")
@Table(name = "book", uniqueConstraints = {@UniqueConstraint(name = "book_uuid", columnNames = "book_uuid")})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @SequenceGenerator(name = "book_sequence", allocationSize = 1, sequenceName = "book_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence")
    @Column(updatable = false, nullable = false)
    @Getter(onMethod = @__(@JsonIgnore))
    private Integer id;

    @Column(nullable = false, columnDefinition = "UUID", name = "book_uuid")
    @Getter
    @Setter
    private UUID bookUUID;

    @Column(nullable = false, columnDefinition = "UUID", name = "chapter_uuid")
    @Getter
    @Setter
    private UUID chapterUUID;

    @Column(nullable = false, name = "is_borrowed")
    @Setter
    @Getter
    private boolean currentlyBorrowed;

    @Column(nullable = false, name = "added_date", columnDefinition = "timestamp")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    @JsonIgnore
    private LocalDate addedDate;

    @Column(name = "deleted_date", columnDefinition = "timestamp")
    @Getter
    @Setter
    private LocalDate deletedDate;

}
