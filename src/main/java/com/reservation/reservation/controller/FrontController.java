package com.reservation.reservation.controller;

import com.reservation.reservation.config.Code;
import com.reservation.reservation.domain.Lecture;
import com.reservation.reservation.domain.LectureApplicant;
import com.reservation.reservation.domain.response.Response;
import com.reservation.reservation.service.Impl.LectureApplicantServiceImpl;
import com.reservation.reservation.service.Impl.LectureServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/front")
public class FrontController {

    private final LectureApplicantServiceImpl lectureApplicantService;
    private final LectureServiceImpl lectureService;
    /**
     * 강연 신청
     */
    @PostMapping("/add")
    Response lectureApplicantAdd(@RequestParam Long lecture_id,
                                 @RequestParam String personelNum) {

        LectureApplicant lectureApplicant = lectureApplicantService.save(lecture_id, personelNum);
        return new Response(Code.OK_CODE, Code.OK_MSG, lectureApplicant);
    }

    /**
     * 강연 목록(신청 가능한 싯점부터 강연시작시간 1일 후까지 노출)
     */
    @GetMapping("/findAll/posible/lecture")
    Response lecutreFindPossible() {
        List<Lecture> posibleLecuture = lectureService.findPosibleLecuture();
        HashMap<String, Object> response = new HashMap<>();
        if(posibleLecuture.size() < 1) {
            return new Response(Code.NO_CONTENT_CODE, Code.NO_CONTENT_MSG, null);
        }
        response.put("totalCnt", posibleLecuture.size());
        response.put("data", posibleLecuture);
        return new Response(Code.OK_CODE, Code.OK_MSG, response);
    }

    /**
     * 신청 내역 조회(사번 입력)
     */
    @GetMapping("/find/applicant")
    Response lecureFindApplicant(@RequestParam String personelNum) {
        List<LectureApplicant> allApplicantMember = lectureApplicantService.findAllApplicantMember(personelNum);
        HashMap<String, Object> response = new HashMap<>();
        if(allApplicantMember.size() < 1) {
            return new Response(Code.NO_CONTENT_CODE, Code.NO_CONTENT_MSG, null);
        }
        response.put("totalCnt", allApplicantMember.size());
        response.put("data", allApplicantMember);
        return new Response(Code.OK_CODE, Code.OK_MSG, response);
    }

    /**
     * 신청한 강연 취소
     */
    @DeleteMapping("/remove/applicant/{lectureApplicant_id}")
    Response lecureDelApplicant(@PathVariable Long lectureApplicant_id) {
        lectureApplicantService.cancelApplicantMember(lectureApplicant_id);
        return new Response(Code.OK_CODE, Code.OK_MSG, "delete Success");
    }

    /**
     * 실시간 인기 강연
     */
    @GetMapping("/find/popular/lecture")
    Response lecturePopularLecture() {
        List<Lecture> popularLecutre = lectureService.findPopularLecuture();
        HashMap<String, Object> response = new HashMap<>();
        if(popularLecutre.size() < 1) {
            return new Response(Code.NO_CONTENT_CODE, Code.NO_CONTENT_MSG, null);
        }
        response.put("totalCnt", popularLecutre.size());
        response.put("data", popularLecutre);
        return new Response(Code.OK_CODE, Code.OK_MSG, response);
    }
}
