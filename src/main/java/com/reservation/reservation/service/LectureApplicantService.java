package com.reservation.reservation.service;

import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.LectureApplicant;
import com.reservation.reservation.domain.request.LectureRequestDto;
import com.reservation.reservation.exception.CustomExcepiton;
import com.reservation.reservation.repository.LectureApplicantRepo;
import com.reservation.reservation.repository.LectureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureApplicantService {
    private final LectureApplicantRepo lectureApplicantRepo;
    private final LectureRepo lectureRepo;
    /**
     * 강연등록
     */
    public LectureApplicant save(Long lecture_id, String personelNum) {
        boolean dupleApplicantCheck = lectureApplicantRepo.findDupleApplicantCheck(personelNum, lecture_id);
        if(!dupleApplicantCheck) {
            throw new CustomExcepiton("사번" + personelNum + "(은)는 이미 강연 신청이 완료되었습니다.");
        }
        Lecture lectureFindOne = lectureRepo.findOne(lecture_id);
        LectureApplicant createApplicant = LectureApplicant.createLectureApplicant(lectureFindOne, personelNum);
        lectureApplicantRepo.save(createApplicant);
        return createApplicant;
    }

    /**
     * 강연 목록(신청 가능한 싯점부터 강연시작시간 1일 후까지 노출)
     */
    public List<LectureApplicant> findAll() {
        return lectureApplicantRepo.findAll();
    }

    /**
     * 신청 내역 조회
     */
    public List<LectureApplicant> findAllApplicantMember(String personelNum) {
        return lectureApplicantRepo.findAllApplicantMember(personelNum);
    }

    /**
     * 신청 내역 취소
     */
    public void remvoeApplicantMember(Long id) {
        LectureApplicant deleteApplicantMember = lectureApplicantRepo.findOne(id);

        /*applicatnts_num 1 감소*/
        Long lectureId = deleteApplicantMember.getLecture().getId();
        Lecture one = lectureRepo.findOne(lectureId);
        one.minusApplicantNum(1);
        lectureApplicantRepo.deleteApplicantMember(deleteApplicantMember);
    }
}
