package com.reservation.reservation.service;

import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.LectureApplicant;
import com.reservation.reservation.exception.CustomExcepiton;

import java.util.List;

public interface LectureApplicantService {
    LectureApplicant save(Long lecture_id, String personelNum);

    /**
     * 강연 목록(신청 가능한 싯점부터 강연시작시간 1일 후까지 노출)
     */
    List<LectureApplicant> findAll();

    /**
     * 신청 내역 조회
     */
    List<LectureApplicant> findAllApplicantMember(String personelNum);

    /**
     * 신청 내역 취소
     */
    void removeApplicantMember(Long id);
}
