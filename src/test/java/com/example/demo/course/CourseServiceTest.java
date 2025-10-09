package com.example.demo.course;

import com.example.demo.memberCard.MemberCard;
import com.example.demo.memberCard.MemberCardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MemberCardRepository studentIdCardRepository;

    @InjectMocks
    private CourseService courseService;


    @Test
    void getCourse() {
        Course course = new Course(UUID.randomUUID(), "course_test", "campus_test", "university_test");
        Mockito.when(courseRepository.findByUuid(course.getUuid())).thenReturn(java.util.Optional.of(course));
        ResponseEntity<Course> responseEntity = courseService.getCourse(course.getUuid());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(responseEntity.getBody(), course);
        Mockito.verify(courseRepository, Mockito.times(1)).findByUuid(course.getUuid());
    }

    @Test
    void getCourses() {
        List<Course> courseCollection = Collections.singletonList(new Course(UUID.randomUUID(), "test", "test", "test"));
        Page courses = Mockito.mock(Page.class);
        Mockito.when(this.courseRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(courses);
        ResponseEntity<List<Course>> responseEntity = courseService.getCourses(0, 5);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
    }

    @Test
    void postCourse() {
        Course course = new Course(null, "course_test", "campus_test", "university_test");
        var responseEntity = courseService.postCourse(course);
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertTrue(responseEntity.getHeaders().containsKey("Location"));
        Assertions.assertEquals(responseEntity.getHeaders().get("Location"), List.of("http://localhost:8083/api/course/" + course.getUuid()));
        Assertions.assertEquals(course, responseEntity.getBody());
        Mockito.verify(courseRepository, Mockito.times(1)).save(course);
    }

    @Test
    void deleteCourse() {
        Course course = new Course(UUID.randomUUID(), "course_test", "campus_test", "university_test");
        Mockito.when(courseRepository.findByUuid(course.getUuid())).thenReturn(java.util.Optional.of(course));
        var responseEntity = this.courseService.deleteCourse(course.getUuid());
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 204);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).contains("course successfully deleted"));
        Mockito.verify(courseRepository, Mockito.times(1)).delete(course);
    }

    @Test
    void updateCourse() {
        var courseRetrieved = new Course(UUID.randomUUID(), "first_iteration", "first_iteration@campus", "first_iteration@university");
        var courseUpdates = new Course(null, "second_iteration", "second_iteration@campus", "second_iteration@university");
        Mockito.when(courseRepository.findByUuid(courseRetrieved.getUuid())).thenReturn(java.util.Optional.of(courseRetrieved));
        ResponseEntity responseEntity = courseService.updateCourse(courseRetrieved.getUuid(), courseUpdates);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(responseEntity.getHeaders().containsKey("Location"));
        Assertions.assertEquals(responseEntity.getHeaders().get("Location"), List.of("http://localhost:8083/api/course/" + courseRetrieved.getUuid()));
        Mockito.verify(courseRepository, Mockito.times(1)).save(courseRetrieved);

    }

    @Test
    void postStudentCourse() {
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        Course course = new Course(UUID.randomUUID(), "course_test", "campus_test", "university_test");
        Mockito.when(courseRepository.findByUuid(course.getUuid())).thenReturn(java.util.Optional.of(course));
        Mockito.when(studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid())).thenReturn(java.util.Optional.of(studentIdCard));
        ResponseEntity<String> responseEntity = courseService.postStudentCourse(course.getUuid(), studentIdCard.getUuid());
        Assertions.assertNotNull(responseEntity);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(course.getStudentIdCards().stream().findFirst().get(), studentIdCard);
        Assertions.assertEquals(studentIdCard.getCourses().stream().findFirst().get(), course);
        Mockito.verify(courseRepository, Mockito.times(1)).save(course);
        Mockito.verify(studentIdCardRepository, Mockito.times(1)).save(studentIdCard);
    }

    @Test
    void deleteStudentCourse() {
        Course course = new Course(UUID.randomUUID(), "course_test", "campus_test", "university_test");
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        course.getStudentIdCards().add(studentIdCard);
        studentIdCard.getCourses().add(course);
        Mockito.when(courseRepository.findByUuid(course.getUuid())).thenReturn(java.util.Optional.of(course));
        Mockito.when(studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid())).thenReturn(java.util.Optional.of(studentIdCard));
        ResponseEntity responseEntity = courseService.deleteStudentCourse(course.getUuid(), studentIdCard.getUuid());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Mockito.verify(studentIdCardRepository, Mockito.times(1)).save(studentIdCard);
    }

    @Test
    void getStudentsCourse() {
        Course course = new Course(UUID.randomUUID(), "course_test", "campus_test", "university_test");
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        MemberCard studentIdCard1 = new MemberCard(UUID.randomUUID());
        course.getStudentIdCards().add(studentIdCard);
        course.getStudentIdCards().add(studentIdCard1);
        Mockito.when(courseRepository.findByUuid(course.getUuid())).thenReturn(java.util.Optional.of(course));
        var responseEntity = courseService.getStudentsCourse(course.getUuid());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).contains(studentIdCard));
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).contains(studentIdCard1));
    }
}