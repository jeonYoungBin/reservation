package com.reservation.reservation.controller;

import com.reservation.reservation.config.Code;
import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.LectureApplicant;
import com.reservation.reservation.domain.request.LectureRequestDto;
import com.reservation.reservation.domain.response.Response;
import com.reservation.reservation.service.LectureApplicantService;
import com.reservation.reservation.service.LectureService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/backoffice")
public class BackOfficeController {

    private final LectureService lectureService;
    private final LectureApplicantService lectureApplicantService;

    /**
     * 강연 등록
     */
    @PostMapping("/add")
    Response lectureAdd(@RequestBody @Valid LectureRequestDto lectureRequestDto) {
        return new Response(Code.OK_CODE, Code.OK_MSG, lectureService.save(lectureRequestDto));
    }

    /**
     * 강연 전체 목록
     */
    @GetMapping("/findAll")
    Response lectureFindAll() {
        List<Lecture> findAlllecture = lectureService.findAll();
        HashMap<String, Object> response = new HashMap<>();
        if(findAlllecture.size() < 1) {
            return new Response(Code.NO_CONTENT_CODE, Code.NO_CONTENT_MSG, null);
        }
        response.put("totalCnt", findAlllecture.size());
        response.put("data", findAlllecture);
        return new Response(Code.OK_CODE, Code.OK_MSG, response);
    }

    /**
     * 강연 신청자 전체 목록
     */
    @GetMapping("/findAll/applicant")
    Response lectureApplicantFindAll() {
        List<LectureApplicant> findAllApplicants = lectureApplicantService.findAll();
        HashMap<String, Object> response = new HashMap<>();
        if(findAllApplicants.size() < 1) {
            return new Response(Code.NO_CONTENT_CODE, Code.NO_CONTENT_MSG, null);
        }
        response.put("totalCnt", findAllApplicants.size());
        response.put("data", findAllApplicants);
        return new Response(Code.OK_CODE, Code.OK_MSG, response);
    }
}
