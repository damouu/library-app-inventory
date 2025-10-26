package com.example.demo.chapter;

import com.example.demo.series.Series;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "chapter")
@Table(name = "chapter", uniqueConstraints = {@UniqueConstraint(name = "chapter_uuid", columnNames = "chapter_uuid")})
@NoArgsConstructor
public class Chapter {
    @Id
    @SequenceGenerator(name = "chapter_sequence", allocationSize = 1, sequenceName = "chapter_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapter_sequence")
    @Column(updatable = false, nullable = false)
    @Getter(onMethod = @__(@JsonIgnore)) // generate the getter with the specific annotation.
    @Setter
    private Integer id;

    @Column(nullable = false, columnDefinition = "UUID", name = "chapter_uuid")
    @Getter
    @Setter
    private UUID chapter_uuid;

    @Column(name = "title", nullable = false)
    @Getter
    @Setter
    private String title;

    @Column(name = "second_title", nullable = false)
    @Getter
    @Setter
    private String second_title;

    @Column(name = "total_pages", nullable = false)
    @Getter
    @Setter
    private Integer totalPages;

    @Column(name = "chapter_number", nullable = false)
    @Getter
    @Setter
    private Integer chapter_number;

    @Column(name = "cover_artwork_URL", nullable = false)
    @Getter
    @Setter
    private String cover_artwork_URL;

    @Column(name = "publication_date", nullable = false, columnDefinition = "Date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    @Getter(onMethod = @__(@JsonIgnore)) // generate the getter with the specific annotation.
    @Setter
    private LocalDate publication_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    private Series series;

    @Column(name = "deleted_at", columnDefinition = "DATE")
    @Getter
    @Setter
    private LocalDate deleted_at;

    @Column(name = "created_at", nullable = false, columnDefinition = "Date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    @Getter(onMethod = @__(@JsonIgnore)) // generate the getter with the specific annotation.
    @Setter
    private LocalDate created_at;


}
