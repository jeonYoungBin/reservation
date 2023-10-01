package com.reservation.reservation.repository;

import com.reservation.reservation.domain.LectureApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface LectureApplicantRepoJpa extends JpaRepository<LectureApplicant, Long> {

    @Query("select distinct a from LectureApplicant a join fetch a.lecture")
    List<LectureApplicant> findAll();

    @Query("select l from LectureApplicant l join fetch l.lecture where l.personelNum = :personelNum")
    List<LectureApplicant> findAllApplicantMember(String personelNum);

    @Query(nativeQuery = true, value = "select * from LECTURE_APPLICANT a inner join LECTURE l ON a.LECTURE_ID = l.LECTURE_ID where a.PERSONEL_NUM = :personelNum AND l.LECTURE_ID = :lectureId")
    Optional<LectureApplicant> findDupleApplicantCheck(String personelNum, Long lectureId);

    @Query(nativeQuery = true, value = "select * from LECTURE_APPLICANT where LECTURE_ID = :lectureApplicantId")
    Optional<LectureApplicant> findOne(Long lectureApplicantId);
}
