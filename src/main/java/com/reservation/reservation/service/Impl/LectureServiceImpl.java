package com.reservation.reservation.service.Impl;

import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.request.LectureRequestDto;
import com.reservation.reservation.exception.CustomExcepiton;
import com.reservation.reservation.repository.LecturRepoJpa;
//import com.reservation.reservation.repository.LectureRepo;
import com.reservation.reservation.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TableGenerator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureServiceImpl implements LectureService {
//    private final LectureRepo lectureRepo;

    private final LecturRepoJpa lectureRepo;
    private final RedissonClient redissonClient;
    private static final int WAIT_TIME = 5;
    private static final int LEASE_TIME = 5;
    private static final String SEAT_LOCK = "seat_lock";
    static final String REGEXP_PATTERN_CHAR = "^[\\d]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-4]):(0[0-9]|[1-5][0-9])$";
    static final Long before7Days = 7L;
    static final Long after1Days = 1L;
    static final Long before3Days = 3L;

    /**
     * 강연 등록
     */
    @Override
    public Lecture save(LectureRequestDto lectureRequestDto) {
        //날짜형식 체크 후 localDateTime으로 변환하여 DB insert
        LocalDateTime lecture_time = datePattern(lectureRequestDto.getLecture_time());
        Lecture lecture = new Lecture(lectureRequestDto, lecture_time);

        lectureRepo.save(lecture);
        return lecture;
    }

    /**
     * 등록된 강연 모든 리스트 출력
     */
    @Override
    public List<Lecture> findAll() {
        return lectureRepo.findAll();
    }

    /**
     * 3일간 가장 신청이 많은 강의(내린 차순으로 정리)
     */
    @Override
    public List<Lecture> findPopularLecuture() {
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime now3MinusDateTime = LocalDateTime.now().minusDays(before3Days);

        return lectureRepo.findPopularLecture(now3MinusDateTime, nowDateTime);
    }

    /**
     * 신청 가능한 강의(7일 전부터 1일 후까지 노출)
     */
    @Override
    public List<Lecture> findPosibleLecuture() {
        List<Lecture> lectures = lectureRepo.findAll();
        ArrayList<Lecture> response = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if(lecture.getLectureTime().minusDays(before7Days).isBefore(LocalDateTime.now()) &&
                    lecture.getLectureTime().plusDays(after1Days).isAfter(LocalDateTime.now())) {
                response.add(lecture);
            }
        }
        return response;
    }

    /**
     * 날짜 패턴 validation check 및 String -> DateTime 변환
     */
    @Override
    public LocalDateTime datePattern(String lectureTime) throws CustomExcepiton {
        boolean matches = Pattern.matches(REGEXP_PATTERN_CHAR, lectureTime);
        if(!matches)
            throw new CustomExcepiton("날짜 패턴이 잘못되었습니다.(올바른 형식 : YYYY-MM-DD HH:MM)");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(lectureTime, formatter);
    }

    /*@Override
    public void updateLikeCount() {
        RLock lock = redissonClient.getLock(SEAT_LOCK);
        try {
            if(!(lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS))) {
                log.error("lock 획득 실패");
                throw new RuntimeException("lock 획득 실패");
            }
            log.info("lock 획득 성공");
            Optional<Lecture> testOne = lectureRepo.findOne(1L);
            if(testOne.isPresent()) {
                testOne.get().likeUpCount();
            }
            lectureRepo.saveAndFlush(testOne.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }*/
}
