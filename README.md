# 개발 언어
JAVA 17
# 프레임 워크
Spring boot 2.7.14
# DBMS
H2 DataBase
# 데이터 설계
#### ERD는 erdcloud 사용
![스크린샷 2023-08-17 오후 3 01 48](https://github.com/jeonYoungBin/reservation/assets/137134782/8d9b49bf-be80-4b06-b408-747163e379df)
# API 설명
## backoffice
1. POST /backoffice/add<br/>
   - 강연 등록(강연장,강연자,강연일시,강연내용 등을 requestBody로 받아 입력)<br/>
   - 강연 날짜 포맷은 YYYY-MM-DD HH:MM 포맷 일치 하지 않을시 exception 처리<br/>
2. GET /backoffice/findAll<br/>
   - 등록된 모든 강연 리스트 출력<br/>
3. GET /backoffice/findAll/applicant<br/>
   - 강연 신청자 전체 목록 리스트 출력<br/>
## front
1. POST /front/add<br/>
   - 강연 신청<br/>
     - 한 사원이 동일한 강연은 신청할 수 없음. 신청시 exception 처리<br/>
     - 강연 신청 후 APPLICANTS_NUM(신청자수) field count 1 증가, modify_time 업데이트<br/>
2. GET /front/findAll/posible/lecture<br/>
   - 신청 가능한 강연 목록 리스트 출력<br/>
     - 강연날짜-7일 < 현재 날짜 < 강연날짜+1일 조건에 맞는 데이터 출력<br/>
3. GET /front/find/applicant<br/>
   - 특정 사번을 입력받아 신청 내역 조회<br/>
4. DELETE /remove/applicant/{lectureApplicant_id}<br/>
   - 신청한 강연 취소<br/>  
     - lecture_applicant pk값을 받아 삭제<br/>
     - 삭제 후에는 LECUTRE APPLICANTS_NUM(신청자수) field count 1 감소<br/>
5. GET /find/popular/lecture<br/>
   - 실시간 인기 강연 출력<br/>
     - 신청자 수 기준으로 내림차순 출력<br/>
     - 최근 3일간 가장 신청이 많다는 기준이 모호하여 최근 업데이트 시간 필드(modify_date)를 기준으로 다음과 같이 조건을 걸어 처리<br/>
       현재시간-3일 < modify_date <= 현재시간<br/>
## 테스트 유의 사항
application.yml 의 jpa.hibernate.ddl-auto 설정이 create로 되어있어야 서비스 동작시 테이블이 생성됩니다.<br/> 
그 이후 서비스를 내렸다 다시 올릴시에 none으로 바꾸어 확인 부탁드립니다.(create로 계속 남아있을시에 기존에 있는 테이블이 삭제되었다 다시 생성됩니다.)
