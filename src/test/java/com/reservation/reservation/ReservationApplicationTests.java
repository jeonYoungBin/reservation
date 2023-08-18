package com.reservation.reservation;

import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.request.LectureRequestDto;
import com.reservation.reservation.exception.CustomExcepiton;
import com.reservation.reservation.repository.LectureApplicantRepo;
import com.reservation.reservation.repository.LectureRepo;
import com.reservation.reservation.service.Impl.LectureApplicantServiceImpl;
import com.reservation.reservation.service.Impl.LectureServiceImpl;
import com.reservation.reservation.service.LectureApplicantService;
import com.reservation.reservation.service.LectureService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ReservationApplicationTests {

	@Autowired
	LectureRepo lectureRepo;

	@Autowired
	LectureApplicantRepo lectureApplicantRepo;

	@Autowired
	LectureService lectureService;

	@Autowired
	LectureApplicantService lectureApplicantService;
	@Test
	void 인기강연리스트() {
		LocalDateTime nowDateTime = LocalDateTime.now();
		LocalDateTime now3MinusDateTime = LocalDateTime.now().minusDays(3L);
		List<Lecture> popularAll = lectureRepo.findCritiaAll(nowDateTime, now3MinusDateTime);
		assertEquals(0, popularAll.size());
	}

	@Test
	void 강연등록() {
		//given
		LectureRequestDto lectureRequestDto = new LectureRequestDto();
		lectureRequestDto.setLecture_hall("강연장");
		lectureRequestDto.setLecture_msg("강연 메시지");
		lectureRequestDto.setLecturer("강연자");
		lectureRequestDto.setLecture_time("2023-08-11 11:00");
		//when
		Lecture saveLecture = lectureService.save(lectureRequestDto);
		//then
		Lecture findLecture = lectureRepo.findOne(saveLecture.getId());
		assertEquals(saveLecture.getId(), findLecture.getId());
	}
	@Test
	void 강연신청자조회() {
		int cnt = lectureApplicantRepo.findAll().size();
		assertEquals(3, cnt);
	}
	@Test
	@Rollback(value = false)
	void 중복신청자테스트() {
		//given
		String personelNum = "12345";
		LectureRequestDto lectureRequestDto = new LectureRequestDto();
		lectureRequestDto.setLecture_hall("강연장");
		lectureRequestDto.setLecture_msg("강연 메시지");
		lectureRequestDto.setLecturer("강연자");
		lectureRequestDto.setLecture_time("2023-08-11 11:00");
		Lecture saveLecture = lectureService.save(lectureRequestDto);
		//when
		lectureApplicantService.save(saveLecture.getId(), personelNum);

		//then
		CustomExcepiton e = assertThrows(CustomExcepiton.class, () -> lectureApplicantService.save(saveLecture.getId(), "12345"));
		assertThat(e.getMessage()).isEqualTo("사번" + personelNum + "(은)는 이미 강연 신청이 완료되었습니다.");
	}



}
