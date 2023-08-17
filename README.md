# 개발 언어
JAVA 17
# 프레임 워크
Spring boot 2.7.14
# DBMS
H2 DataBase
# 데이터 설계
![스크린샷 2023-08-17 오후 3 01 48](https://github.com/jeonYoungBin/reservation/assets/137134782/8d9b49bf-be80-4b06-b408-747163e379df)
# API 설명 
<backoffice>
1. POST /backoffice/add
   - 강연 등록(강연장,강연자,강연일시,강연내용 등을 requestBody로 받아 입력)
   - 강연 날짜 포맷은 YYYY-MM-DD HH:MM 포맷 일치 하지 않을시 exception 처리
2. GET /backoffice/findAll
   - 등록된 모든 강연 리스트 출력
3. GET /backoffice/findAll/applicant
   - 강연 신청자 전체 목록 리스트 출력
<front>
1. POST /front/add
   - 강연 신청
     - 한 사원이 동일한 강연은 신청할 수 없음. 신청시 exception 처리
     - 강연 신청 후 LECUTRE APPLICANTS_NUM(신청자수) field count 1 증가, modify_time 업데이트
2. GET /front/findAll/posible/lecture
   - 신청 가능한 강연 목록 리스트 출력
     - 강연날짜-7일 < 현재 날짜 < 강연날짜+1일 조건에 맞는 데이터 출력
3. GET /front/find/applicant
   - 특정 사번을 입력받아 신청 내역 조회
     - Request param에 사번을 받음
4. DELETE /remove/applicant/{lectureApplicant_id}
   - 신청한 강연 취소  
     - lecture_applicant pk값을 받아 삭제
     - 삭제 후에는 LECUTRE APPLICANTS_NUM(신청자수) field count 1 감소
5. GET /find/popular/lecture
   - 실시간 인기 강연 출력
     - 신청자 수 기준으로 내림차순 출력
     - 최근 3일간 가장 신청이 많다는 기준이 모호하여 최근 업데이트 시간 필드(modify_date)를 기준으로 다음과 같이 조건을 걸어 처리
       현재시간-3일 < modify_date <= 현재시간 
