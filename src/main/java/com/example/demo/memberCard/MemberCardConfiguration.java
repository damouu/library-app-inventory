package com.example.demo.memberCard;

import com.example.demo.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MemberCardConfiguration {

//    @Bean
    CommandLineRunner runner(MemberCardRepository serializable, StudentRepository studentRepository) {
        return args -> {
            for (int i = 1; i < 20; i++) {
                MemberCard student_idCard = new MemberCard(UUID.randomUUID());
//                Student student = studentRepository.findById(i).get();
//                student_idCard.setStudent(student);
                serializable.save(student_idCard);
            }
        };
    }
}
