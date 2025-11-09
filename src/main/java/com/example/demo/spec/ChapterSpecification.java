package com.example.demo.spec;

import com.example.demo.model.Chapter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChapterSpecification {

    public static Specification<Chapter> filterChapter(Map allParams) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            String chapterNumberStr = (String) allParams.get("chapter_number");

            if (org.apache.commons.lang3.StringUtils.isNotBlank(chapterNumberStr)) {

                try {

                    Integer chapterNumberInt = Integer.parseInt(chapterNumberStr);
                    System.out.println("Parsed chapter number: " + chapterNumberInt);
                    predicates.add(criteriaBuilder.equal(root.get("chapter_number"), chapterNumberInt));

                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("chapter number must be a number", e);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}