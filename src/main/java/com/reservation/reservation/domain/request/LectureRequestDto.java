package com.reservation.reservation.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class LectureRequestDto {
    //강연자
    @NotNull(message = "강연자를 입력해주세요")
    private String lecturer;
    //강연장
    @NotNull(message = "강연장을 입력해주세요")
    private String lecture_hall;
    //강연 시간
    @NotNull(message = "강연시간을 입력해주세요")
    private String lecture_time;
    //강연 내용
    @NotNull(message = "강연내용을 입력해주세요")
    private String lecture_msg;
}
