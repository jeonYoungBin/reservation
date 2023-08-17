package com.reservation.reservation.repository;

import com.reservation.reservation.domain.Lecture;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureRepo {

    private final EntityManager em;

    @Transactional
    public void save(Lecture le) {
        em.persist(le);
    }

    public List<Lecture> findAll() {
        return em.createQuery("select l from Lecture l", Lecture.class)
                .getResultList();
    }

    public Lecture findOne(Long id) {
        return em.find(Lecture.class, id);
    }

    public List<Lecture> findCritiaAll(LocalDateTime nowDateTime, LocalDateTime nowMinusDateTime) {
        return em.createQuery("select l from Lecture l where l.modifyDate > ?1 AND l.modifyDate <= ?2 order by l.applicantsNum desc", Lecture.class)
                .setParameter(1, nowMinusDateTime)
                .setParameter(2, nowDateTime)
                .getResultList();
    }
}
