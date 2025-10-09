package com.example.demo.course;

import com.example.demo.memberCard.MemberCard;
import com.example.demo.memberCard.MemberCardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CourseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MemberCardRepository studentIdCardRepository;


    @Test
    void getCourses() {
        ResponseEntity<List> responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/api/course?size=5&page=1", List.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).includes(MediaType.APPLICATION_JSON));
        Assertions.assertFalse(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
        Assertions.assertEquals((long) Objects.requireNonNull(responseEntity.getBody()).size(), 5);
    }


    @Test
    void getCourse() {
        ResponseEntity<Course> responseEntity =
                this.restTemplate.getForEntity("http://localhost:" + port + "/api/course/" + courseRepository.findById(29).get().getUuid(), Course.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).includes(MediaType.APPLICATION_JSON));
        Assertions.assertNotNull(responseEntity.getBody());
    }

    @Test
    void deleteCourse() {
        restTemplate.delete("http://localhost:" + port + "/api/course/" + courseRepository.findById(26).get().getUuid());
        Assertions.assertTrue(courseRepository.findById(26).isEmpty());
    }

    @Test
    void postCourse() {
        Course course = new Course(null, "course_test", "campus_test", "university_test");
        var responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/course", course, Course.class);
        var courseRetrieved = courseRepository.findByUuid(Objects.requireNonNull(responseEntity.getBody()).getUuid());
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertTrue(responseEntity.getHeaders().containsKey("Location"));
        Assertions.assertEquals(course.getName(), Objects.requireNonNull(responseEntity.getBody()).getName());
        Assertions.assertEquals(course.getCampus(), responseEntity.getBody().getCampus());
        Assertions.assertEquals(course.getUniversity(), responseEntity.getBody().getUniversity());
        Assertions.assertNotNull(courseRetrieved.get());
    }

    @Test
    void updateCourse() {
        var courseRetrieved = new Course(UUID.randomUUID(), "first_iteration", "first_iteration@campus", "first_iteration@university");
        var courseUpdates = new Course(UUID.randomUUID(), "second_iteration", "second_iteration@campus", "second_iteration@university");
        courseRepository.save(courseRetrieved);
        HttpEntity<Course> entity = new HttpEntity<Course>(courseUpdates);
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/course/" + courseRetrieved.getUuid(),
                HttpMethod.PUT, entity, String.class);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(responseEntity.getHeaders().containsKey("Location"));
        Assertions.assertEquals(responseEntity.getHeaders().get("Location"), List.of("http://localhost:8083/api/course/" + courseRetrieved.getUuid()));
    }

    @Test
    void getStudentsCourse() {
        Course course = new Course(UUID.randomUUID(), "course_test", "campus_test", "university_test");
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        MemberCard studentIdCard1 = new MemberCard(UUID.randomUUID());
        course.getStudentIdCards().add(studentIdCard);
        course.getStudentIdCards().add(studentIdCard1);
        studentIdCard.getCourses().add(course);
        studentIdCard1.getCourses().add(course);
        courseRepository.save(course);
        ResponseEntity<MemberCard[]> responseEntity = restTemplate.getForEntity
                ("http://localhost:" + port + "/api/course/" + course.getUuid() + "/student", MemberCard[].class);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        Assertions.assertTrue(Arrays.stream(Objects.requireNonNull(responseEntity.getBody())).count() > 0);
    }

    @Test
    void deleteStudentCourse() {
        Course course = new Course(UUID.randomUUID(), "course_test", "campus_test", "university_test");
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        course.getStudentIdCards().add(studentIdCard);
        studentIdCard.getCourses().add(course);
        courseRepository.save(course);
        studentIdCardRepository.save(studentIdCard);
        Optional<Course> course1 = courseRepository.findByUuid(course.getUuid());
        Assertions.assertFalse(course1.get().getStudentIdCards().isEmpty());
        Assertions.assertTrue(course1.get().getStudentIdCards().stream().anyMatch(card -> card.getUuid().equals(studentIdCard.getUuid())));
        restTemplate.delete("http://localhost:" + port + "/api/course/" + course.getUuid() + "/student/" + studentIdCard.getUuid());
        var optionalCourse = courseRepository.findByUuid(course.getUuid());
        var optionalStudentIdCard = studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid());
        Assertions.assertTrue(optionalCourse.get().getStudentIdCards().isEmpty());
        Assertions.assertTrue(optionalStudentIdCard.get().getCourses().isEmpty());
    }

    @Test
    void postStudentCourse() {
        Course course = new Course(UUID.randomUUID(), "course_test", "campus_test", "university_test");
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        courseRepository.save(course);
        studentIdCardRepository.save(studentIdCard);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/course/" + course.getUuid() + "/student/" + studentIdCard.getUuid(), null, String.class);
        Assertions.assertNotNull(responseEntity);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(responseEntity.getBody(), "student card" + " " + studentIdCard.getUuid() + " " + "added to the course" + " " + course.getUuid());
        var optionalCourse = courseRepository.findByUuid(course.getUuid());
        var optionalStudentIdCard = studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid());
        Assertions.assertEquals(optionalCourse.get().getStudentIdCards().stream().findFirst().get().getUuid(), studentIdCard.getUuid());
        Assertions.assertEquals(optionalStudentIdCard.get().getCourses().stream().findFirst().get().getUuid(), course.getUuid());
    }

}