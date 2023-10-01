package com.reservation.reservation.service.Impl;

import com.reservation.reservation.config.Code;
import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.LectureApplicant;
import com.reservation.reservation.exception.CustomExcepiton;
import com.reservation.reservation.repository.LecturRepoJpa;
//import com.reservation.reservation.repository.LectureApplicantRepo;
import com.reservation.reservation.repository.LectureApplicantRepoJpa;
//import com.reservation.reservation.repository.LectureRepo;
import com.reservation.reservation.service.LectureApplicantService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LectureApplicantServiceImpl implements LectureApplicantService {
    //    private final LectureApplicantRepo lectureApplicantRepo;
//    private final LectureRepo lectureRepo;
    private final LectureApplicantRepoJpa lectureApplicantRepo;
    private final LecturRepoJpa lectureRepo;
    private final RedissonClient redissonClient;
    private static final int WAIT_TIME = 5;
    private static final int LEASE_TIME = 5;
    private static final String SEAT_LOCK = "seat_lock";

    /**
     * 강연등록
     */
    @Override
    @Transactional
    public LectureApplicant save(Long lecture_id, String personelNum) {
        RLock lock = redissonClient.getLock(SEAT_LOCK);
        try {
            /*lock 획득*/
            if(!(lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS))) {
                throw new RuntimeException("lock 획득 실패");
            }
            /*강의 여부 조회 없으면 exception*/
            Optional<Lecture> lectureFindOne = lectureRepo.findOne(lecture_id);
            if(lectureFindOne.isEmpty()) {
                throw new CustomExcepiton("해당되는 강의가 없습니다.");
            }
            Optional<LectureApplicant> dupleApplicantCheck = lectureApplicantRepo.findDupleApplicantCheck(personelNum, lecture_id);
            if(dupleApplicantCheck.isPresent()) {
                throw new CustomExcepiton("사번" + personelNum + " (은)는 이미 강연 신청이 완료되었습니다.");
            }
            LectureApplicant createApplicant = LectureApplicant.createLectureApplicant(lectureFindOne.get(), personelNum);
            lectureApplicantRepo.saveAndFlush(createApplicant);

            return createApplicant;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 강연 목록(신청 가능한 싯점부터 강연시작시간 1일 후까지 노출)
     */
    @Override
    public List<LectureApplicant> findAll() {
        return lectureApplicantRepo.findAll();
    }

    /**
     * 신청 내역 조회
     */
    @Override
    public List<LectureApplicant> findAllApplicantMember(String personelNum) {
        return lectureApplicantRepo.findAllApplicantMember(personelNum);
    }

    /**
     * 신청 내역 취소
     */
    @Override
    @Transactional
    public void cancelApplicantMember(Long id) {
        RLock lock = redissonClient.getLock(SEAT_LOCK);
        try {
            if(!(lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS))) {
                throw new RuntimeException("lock 획득 실패");
            }
            Optional<LectureApplicant> deleteApplicantMember = lectureApplicantRepo.findById(id);
            if(deleteApplicantMember.isEmpty()) {
                throw new CustomExcepiton("해당 신청자가 없습니다.");
            }
            Long lectureId = deleteApplicantMember.get().getLecture().getId();
            /*applicatnts_num 1 감소*/
            Optional<Lecture> delApplicants = lectureRepo.findOne(lectureId);
            if(delApplicants.isPresent())
                delApplicants.get().minusApplicantNum(1);
            lectureRepo.saveAndFlush(delApplicants.get());
            /*applicatnts 삭제*/
            lectureApplicantRepo.delete(deleteApplicantMember.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
