package com.reservation.reservation.service;

import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.request.LectureRequestDto;
import com.reservation.reservation.exception.CustomExcepiton;
import com.reservation.reservation.repository.LectureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepo lectureRepo;
    static final String REGEXP_PATTERN_CHAR = "^[\\d]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-4]):(0[0-9]|[1-5][0-9])$";
    /**
     * 강연 등록
     */
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
    public List<Lecture> findAll() {
        return lectureRepo.findAll();
    }

    /**
     * 3일간 가장 신청이 많은 강의(내린 차순으로 정리)
     */
    public List<Lecture> findPopularLecuture() {
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime now3MinusDateTime = LocalDateTime.now().minusDays(3L);

        return lectureRepo.findCritiaAll(nowDateTime, now3MinusDateTime);
    }

    /**
     * 신청 가능한 강의(7일 전부터 1일 후까지 노출)
     */
    public List<Lecture> findPosibleLecuture() {
        List<Lecture> lectures = lectureRepo.findAll();
        ArrayList<Lecture> response = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if(lecture.getLectureTime().minusDays(7L).isBefore(LocalDateTime.now()) &&
                    lecture.getLectureTime().plusDays(1L).isAfter(LocalDateTime.now())) {
                response.add(lecture);
            }
        }
        return response;
    }

    /**
     * 날짜 패턴 validation check 및 String -> DateTime 변환
     */
    public LocalDateTime datePattern(String lectureTime) throws CustomExcepiton {
        boolean matches = Pattern.matches(REGEXP_PATTERN_CHAR, lectureTime);
        if(!matches)
            throw new CustomExcepiton("날짜 패턴이 잘못되었습니다.(올바른 형식 : YYYY-MM-DD HH:MM)");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(lectureTime, formatter);
    }

}
