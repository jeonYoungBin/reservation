package com.reservation.reservation;

import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.LectureApplicant;
import com.reservation.reservation.domain.request.LectureRequestDto;
import com.reservation.reservation.exception.CustomExcepiton;
import com.reservation.reservation.repository.LecturRepoJpa;
//import com.reservation.reservation.repository.LectureApplicantRepo;
import com.reservation.reservation.repository.LectureApplicantRepoJpa;
//import com.reservation.reservation.repository.LectureRepo;
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
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ReservationApplicationTests {

	//@Autowired
	private final LectureApplicantRepoJpa lectureApplicantRepo;

	//@Autowired
	private final LecturRepoJpa lecturRepo;



	@Autowired
	LectureService lectureService;

	@Autowired
	LectureApplicantService lectureApplicantService;

	ReservationApplicationTests(LectureApplicantRepoJpa lectureApplicantRepo, LecturRepoJpa lecturRepo) {
		this.lectureApplicantRepo = lectureApplicantRepo;
		this.lecturRepo = lecturRepo;
	}

	@Test
	void 테스트(){
		Optional<Lecture> one = lecturRepo.findOne(1L);
		System.out.println(one.get().getLecture_hall());
		assertThat(lectureApplicantRepo.findAll()).hasSize(1);
	}

	@Test
	void 인기강연리스트() {
		LocalDateTime nowDateTime = LocalDateTime.now();
		LocalDateTime now3MinusDateTime = LocalDateTime.now().minusDays(3L);
		List<Lecture> popularAll = lecturRepo.findPopularLecture(nowDateTime, now3MinusDateTime);
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
//		Lecture findLecture = lectureRepo.findOne(saveLecture.getId());
//		assertEquals(saveLecture.getId(), findLecture.getId());
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
		assertThat(e.getMessage()).isEqualTo("사번" + personelNum + " (은)는 이미 강연 신청이 완료되었습니다.");
	}

    /*@Test
    void 동시성테스트() throws InterruptedException {
		int threadCount = 20;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			service.submit(() -> {
				try {
					lectureService.updateLikeCount();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

        Optional<Lecture> testOne = lecturRepo.findOne(1L);
        assertEquals(20, testOne.get().getLikeUpCount());
    }*/

	@Test
	void 동시성테스트1() throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(3);
		CountDownLatch latch = new CountDownLatch(3);
		service.submit(() -> {
			try {
				lectureApplicantService.save(6L, "22222");
			} finally {
				latch.countDown();
			}
		});

		service.submit(() -> {
			try {
				lectureApplicantService.save(6L, "33333");
			} finally {
				latch.countDown();
			}
		});

		service.submit(() -> {
			try {
				lectureApplicantService.save(6L, "77777");
			} finally {
				latch.countDown();
			}
		});

		latch.await();

		Optional<Lecture> testOne = lecturRepo.findOne(6L);
		assertEquals(3, testOne.get().getApplicantsNum());

	}

	@Test
	void 삭제동시성테스트() throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(3);
		CountDownLatch latch = new CountDownLatch(3);
		service.submit(() -> {
			try {
				lectureApplicantService.cancelApplicantMember(30L);
			} finally {
				latch.countDown();
			}
		});

		service.submit(() -> {
			try {
				lectureApplicantService.cancelApplicantMember(31L);
			} finally {
				latch.countDown();
			}
		});

		service.submit(() -> {
			try {
				lectureApplicantService.cancelApplicantMember(32L);
			} finally {
				latch.countDown();
			}
		});

		latch.await();

		Optional<Lecture> testOne = lecturRepo.findOne(6L);
		assertEquals(0, testOne.get().getApplicantsNum());

	}

	@Test
	void 조인쿼리문테스트() {
		List<LectureApplicant> all = lectureApplicantRepo.findAll();
		for (LectureApplicant lectureApplicant : all) {
			System.out.println(lectureApplicant.getPersonelNum());
		}
	}

	@Test
	void 조인쿼리문테스트1() {
		List<LectureApplicant> all = lectureApplicantRepo.findAllApplicantMember("12345");
		for (LectureApplicant lectureApplicant : all) {
			System.out.println(lectureApplicant.getPersonelNum());
		}
	}

	@Test
	void 조인쿼리문테스트2() {
		Optional<LectureApplicant> dupleApplicantCheck = lectureApplicantRepo.findDupleApplicantCheck("12345", 1L);
		System.out.println(dupleApplicantCheck.isPresent());
	}

	@Test
	void 삭제테스트() {
		Optional<LectureApplicant> byId = lectureApplicantRepo.findById(4L);
		if(byId.isPresent()) {
			lectureApplicantRepo.delete(byId.get());
		}

	}

}
