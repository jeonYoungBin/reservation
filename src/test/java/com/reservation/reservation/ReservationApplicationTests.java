package com.reservation.reservation;

import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.LectureApplicant;
import com.reservation.reservation.repository.LectureApplicantRepo;
import com.reservation.reservation.repository.LectureRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class ReservationApplicationTests {

	@Autowired
	LectureRepo lectureRepo;

	@Autowired
	LectureApplicantRepo lectureApplicantRepo;

	@Test
	void contextLoads() {
		LocalDateTime nowDateTime = LocalDateTime.now();
		LocalDateTime now3MinusDateTime = LocalDateTime.now().minusDays(3L);
		List<Lecture> popularAll = lectureRepo.findCritiaAll(nowDateTime, now3MinusDateTime);
		Assertions.assertEquals(0, popularAll.size());
	}

	@Test
	void 날짜변환테스트() {

	}

	@Test
	void 중복회원테스트() {
		String personelNum = "12345";
		Long fk_id = 11L;
		boolean flag = lectureApplicantRepo.findDupleApplicantCheck(personelNum, fk_id);
		Assertions.assertEquals(false, flag);
	}

}
