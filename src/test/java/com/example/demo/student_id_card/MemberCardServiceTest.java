package com.example.demo.student_id_card;

import com.example.demo.course.Course;
import com.example.demo.memberCard.MemberCardRepository;
import com.example.demo.memberCard.MemberCardService;
import com.example.demo.memberCard.MemberCard;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class MemberCardServiceTest {

    @Mock
    private MemberCardRepository studentIdCardRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private MemberCardService memberCardService;


    @Test
    void getStudentIdCards() {
        List<MemberCard> studentIdCards = Arrays.asList(new MemberCard(UUID.randomUUID()), new MemberCard(UUID.randomUUID()));
        Page page = Mockito.mock(Page.class);
        Mockito.when(this.studentIdCardRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        ResponseEntity<List<MemberCard>> responseEntity = memberCardService.getStudentIdCards(0, 5);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
    }

    @Test
    void getStudentIdCard() {
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        Mockito.when(studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid())).thenReturn(java.util.Optional.of(studentIdCard));
        var studentIdCard1 = memberCardService.getStudentIdCard(studentIdCard.getUuid());
        Assertions.assertNotNull(studentIdCard1);
        Assertions.assertEquals(studentIdCard.getUuid(), studentIdCard1.getUuid());
        Mockito.verify(studentIdCardRepository, Mockito.times(1)).findStudentIdCardByUuid(studentIdCard.getUuid());
    }

    @Test
    void deleteStudentIdCard() {
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        Mockito.when(studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid())).thenReturn(java.util.Optional.of(studentIdCard));
        ResponseEntity responseEntity = memberCardService.deleteStudentIdCard(studentIdCard.getUuid());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Mockito.verify(studentIdCardRepository, Mockito.times(1)).delete(studentIdCard);
    }

    @Test
    void postStudentIdCard() {
        Student student = new Student(UUID.randomUUID(), "test_postStudentIdCard", LocalDate.of(2000, Month.APRIL, 7), "test_postStudentIdCard@yahoo.com");
        Mockito.when(studentRepository.findStudentByUuid(student.getUuid())).thenReturn(java.util.Optional.of(student));
        ResponseEntity<MemberCard> responseEntity = memberCardService.postStudentIdCard(student.getUuid());
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 201);
        Assertions.assertEquals(responseEntity.getHeaders().getLocation(), URI.create("http://localhost:8083/api/studentCard/" + Objects.requireNonNull(responseEntity.getBody()).getUuid()));
        Assertions.assertEquals(student, responseEntity.getBody().getStudent());
        Mockito.verify(studentIdCardRepository, Mockito.times(1)).save(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Test
    void getStudentIdCardCourse() {
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        Course course = new Course(UUID.randomUUID(), "course_test_name", "course_test_campus", "course_test_university");
        Course course1 = new Course(UUID.randomUUID(), "course1_test_name", "course1_test_campus", "course1_test_university");
        studentIdCard.getCourses().add(course);
        studentIdCard.getCourses().add(course1);
        Mockito.when(studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid())).thenReturn(java.util.Optional.of(studentIdCard));
        ResponseEntity<Collection<Course>> responseEntity = memberCardService.getStudentIdCardCourse(studentIdCard.getUuid());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertFalse(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
        Mockito.verify(studentIdCardRepository, Mockito.times(1)).findStudentIdCardByUuid(studentIdCard.getUuid());
    }

    @Test
    void getStudentStudentIdCard() {
        Student student = new Student(UUID.randomUUID(), "dede", LocalDate.of(2000, Month.AUGUST, 11), "dedeUnit@msn.com");
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        studentIdCard.setStudent(student);
        Mockito.when(studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid())).thenReturn(java.util.Optional.of(studentIdCard));
        var studentIdCard1 = studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid());
        Assertions.assertNotNull(studentIdCard1);
        Assertions.assertEquals(student, studentIdCard1.get().getStudent());
        Assertions.assertNotNull(studentIdCard1.get().getStudent());
        Assertions.assertEquals(studentIdCard1.get().getStudent().getName(), "dede");
        Assertions.assertEquals(studentIdCard1.get().getStudent().getEmail(), "dedeUnit@msn.com");
    }


}