package com.example.demo.student_id_card;

import com.example.demo.memberCard.MemberCardRepository;
import com.example.demo.memberCard.MemberCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class StudentIdCardRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberCardRepository studentIdCardRepository;

    @Test
    public void findStudentIdCardByUuid() {
        MemberCard studentIdCard = new MemberCard(UUID.randomUUID());
        entityManager.persist(studentIdCard);
        entityManager.flush();
        Optional<MemberCard> optional = studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid());
        Assertions.assertFalse(optional.isEmpty());
    }
}
