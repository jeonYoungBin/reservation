package com.reservation.reservation.service;

import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.request.LectureRequestDto;
import com.reservation.reservation.exception.CustomExcepiton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public interface LectureService {
    Lecture save(LectureRequestDto lectureRequestDto);

    /**
     * 등록된 강연 모든 리스트 출력
     */
    List<Lecture> findAll();

    /**
     * 3일간 가장 신청이 많은 강의(내린 차순으로 정리)
     */
    List<Lecture> findPopularLecuture();

    /**
     * 신청 가능한 강의(7일 전부터 1일 후까지 노출)
     */
    List<Lecture> findPosibleLecuture();

    /**
     * 날짜 패턴 validation check 및 String -> DateTime 변환
     */
    LocalDateTime datePattern(String lectureTime);

//    void updateLikeCount();
}
