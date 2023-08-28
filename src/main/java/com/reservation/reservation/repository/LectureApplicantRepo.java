package com.reservation.reservation.repository;

import com.reservation.reservation.domain.LectureApplicant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository

@RequiredArgsConstructor
public class LectureApplicantRepo {
    private final EntityManager em;

    public void save(LectureApplicant lectureApplicant) {
        em.persist(lectureApplicant);
    }
    @Transactional(readOnly = true)
    public List<LectureApplicant> findAll() {
        return em.createQuery("select l from LectureApplicant l join fetch l.lecture", LectureApplicant.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public LectureApplicant findOne(Long id) {
        return em.find(LectureApplicant.class, id);
    }

    @Transactional(readOnly = true)
    public List<LectureApplicant> findAllApplicantMember(String personelNum) {
        return em.createQuery("select l from LectureApplicant l join fetch l.lecture where l.personelNum = :personelNum", LectureApplicant.class)
                .setParameter("personelNum", personelNum)
                .getResultList();
    }

    public void deleteApplicantMember(LectureApplicant deleteApplicantMember) {
        em.remove(deleteApplicantMember);
    }

    @Transactional(readOnly = true)
    public boolean findDupleApplicantCheck(String personelNum, Long lectureId) {
        String query = "select a from LectureApplicant a join a.lecture l " +
                "where a.personelNum = :personelNum and l.id = :id";

        return em.createQuery(query, LectureApplicant.class)
                .setParameter("personelNum", personelNum)
                .setParameter("id", lectureId)
                .getResultList()
                .isEmpty();
    }

}
