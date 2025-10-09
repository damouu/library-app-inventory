package com.example.demo.student_id_card;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.memberCard.MemberCardRepository;
import com.example.demo.memberCard.MemberCard;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StudentIdCardIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberCardRepository studentIdCardRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void getStudentStudentIdCard() {
        Student student = new Student(UUID.randomUUID(), "Tidus",
                LocalDate.of(2000, Month.JANUARY, 21), "tidus.finalfantasy@hotmail.com");
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        studentIdCard.setStudent(student);
        studentIdCardRepository.save(studentIdCard);
        ResponseEntity<Student> responseEntity =
                this.restTemplate.getForEntity("http://localhost:" + port + "/api/studentCard/" + studentIdCard.getUuid() + "/student", Student.class);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(responseEntity.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON));
        Assertions.assertEquals(responseEntity.getBody().getEmail(), "tidus.finalfantasy@hotmail.com");
        Assertions.assertEquals(responseEntity.getBody().getName(), "Tidus");
        Assertions.assertEquals(responseEntity.getBody().getDob(), LocalDate.of(2000, Month.JANUARY, 21));
    }

    @Test
    void deleteStudentIdCard() {
        Student student = new Student(UUID.randomUUID(), "Tidus",
                LocalDate.of(2000, Month.JANUARY, 21), "tidus.finalfantasy@hotmail.com");
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        studentIdCard.setStudent(student);
        studentIdCardRepository.save(studentIdCard);
        restTemplate.delete("http://localhost:" + port + "/api/studentCard/" + studentIdCard.getUuid());
        Assertions.assertTrue(studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid()).isEmpty());
    }

    @Test
    void getStudentIdCards() {
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/studentCard/", List.class);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).isCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertFalse(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
        Assertions.assertTrue((long) responseEntity.getBody().size() > 0);
    }

    @Test
    void postStudentIdCard() {
        Optional<Student> student = studentRepository.findById(7);
        ResponseEntity<MemberCard> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/studentCard/student/" + student.get().getUuid(), null, MemberCard.class);
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 201);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).isCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertEquals(responseEntity.getHeaders().getLocation(), URI.create("http://localhost:8083/api/studentCard/" + Objects.requireNonNull(responseEntity.getBody()).getUuid()));
    }

    @Test
    void getStudentIdCardCourse() {
        Optional<MemberCard> studentIdCard = studentIdCardRepository.findById(10);
        Optional<Course> course = courseRepository.findById(1);
        Optional<Course> course1 = courseRepository.findById(2);
        studentIdCard.get().getCourses().add(course.get());
        studentIdCard.get().getCourses().add(course1.get());
        studentIdCardRepository.save(studentIdCard.get());
        var responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/studentCard/" + studentIdCard.get().getUuid() + "/course", Collection.class);
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).isCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertNotNull(responseEntity.getBody());
    }

    @Test
    void getStudentIdCard() {
        Optional<MemberCard> studentIdCard = studentIdCardRepository.findById(1);
        var responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/studentCard/" + studentIdCard.get().getUuid(), MemberCard.class);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).isCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(studentIdCard.get().getUuid(), Objects.requireNonNull(responseEntity.getBody()).getUuid());
    }

}
