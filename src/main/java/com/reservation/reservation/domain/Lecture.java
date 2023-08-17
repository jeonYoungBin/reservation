package com.reservation.reservation.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reservation.reservation.domain.base.Base;
import com.reservation.reservation.domain.request.LectureRequestDto;
import com.reservation.reservation.exception.CustomExcepiton;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Lecture extends Base {
    @Id @GeneratedValue
    @Column(name = "lecture_id")
    private Long id;
    //강연자
    private String lecturer;
    //강연장
    private String lecture_hall;
    //신청 인원
    private int applicantsNum;
    //강연 시간
    private LocalDateTime lectureTime;
    //강연 내용
    private String lectureContent;
    //수정 날짜(신청자가 count 될때마다 실시간으로 업데이트)
    @JsonIgnore
    @Column(name = "modify_time", nullable = false)
    private LocalDateTime modifyDate = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "lecture")
    private List<LectureApplicant> lectureApplicants = new ArrayList<>();

    public Lecture(LectureRequestDto request, LocalDateTime lecture_time) {
        this.lecturer = request.getLecturer();
        this.lecture_hall = request.getLecture_hall();
        this.lectureTime = lecture_time;
        this.lectureContent = request.getLecture_msg();
        this.applicantsNum = 0;
    }

    public void plusApplicantNum(int cnt) {
        this.modifyDate = LocalDateTime.now();
        this.applicantsNum = this.applicantsNum + cnt;
    }

    public void minusApplicantNum(int cnt) {
        int restApplicantsNum = this.applicantsNum - cnt;
        if(restApplicantsNum < 0) {
            throw new CustomExcepiton("신청 인원은 0이하가 될수 없습니다.");
        }
        this.applicantsNum = this.applicantsNum - cnt;
    }
}
