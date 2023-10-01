package com.reservation.reservation.repository;

import com.reservation.reservation.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LecturRepoJpa extends JpaRepository<Lecture, Long> {
    @Query(nativeQuery = true, value = "select * from Lecture")
    List<Lecture> findAll();

    @Query("select l from Lecture l where l.modifyDate > :nowMinusDateTime AND l.modifyDate <= :nowDateTime order by l.applicantsNum desc")
    List<Lecture> findPopularLecture(LocalDateTime nowMinusDateTime, LocalDateTime nowDateTime);

    @Query("select l from Lecture l where l.id = :id")
    Optional<Lecture> findOne(Long id);
}
