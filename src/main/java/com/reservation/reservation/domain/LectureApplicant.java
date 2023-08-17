package com.reservation.reservation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reservation.reservation.domain.base.Base;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class LectureApplicant extends Base {
    @Id
    @GeneratedValue
    @Column(name = "lectureApplicant_id")
    private Long id;

    //사번
    private String personelNum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    public static LectureApplicant createLectureApplicant(Lecture lecture, String num) {
        LectureApplicant lectureApplicant = new LectureApplicant();
        lectureApplicant.setLecture(lecture);
        lectureApplicant.setPersonelNum(num);
        lecture.plusApplicantNum(1);
        return lectureApplicant;
    }
}
