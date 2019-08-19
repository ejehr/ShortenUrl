## [소개]

8자리 이하 단축URL 서비스
url 암호화 (sha256) 3자리+ DB 시퀀스 base62변환를 이용하여 url을 변환
DB테이블 내에서 변환된 주소와 중복체크 함
기존 변환된 주소가 있을시에는 redirect 함

## [개발환경]

* java 1.8
* tomcat 7.0
* oracle 11g
* eclipse

## [설명]

* JSP로 간략히 입력폼 생성
* Shortening Key 8 Character 이내로 JAVA에서 SHA256 3자리 + sequence 숫자 base62 변환으로 주소처리
* 기존에 저장된 URL 은 DB 조회 후 표시 (중복 X)
* Shortening URL 호출 시 원래 URL로 redirect 처리

## [실행방법]

* db 테이블 생성 
<pre><code>
DROP TABLE HS_URL_001 CASCADE CONSTRAINTS;

CREATE TABLE HS_URL_001
(
  ORGN_URL     VARCHAR2(4000 BYTE)              NOT NULL,
  SEQ_NO       NUMBER,
  SHORTEN_URL  VARCHAR2(4000 BYTE),
  INS_YMDHMS   TIMESTAMP(6) WITH TIME ZONE
);

COMMENT ON TABLE HS_URL_001 IS '(HS_URL_001) URL목록';

COMMENT ON COLUMN HS_URL_001.ORGN_URL IS '실제URL';

COMMENT ON COLUMN HS_URL_001.SEQ_NO IS '일련번호';

COMMENT ON COLUMN HS_URL_001.SHORTEN_URL IS '단축URL';

COMMENT ON COLUMN HS_URL_001.INS_YMDHMS IS '입력일시';

CREATE UNIQUE INDEX HS_URL_001_PK ON HS_URL_001
(ORGN_URL, SEQ_NO);

ALTER TABLE HS_URL_001 ADD (
  CONSTRAINT HS_URL001_PK
  PRIMARY KEY
  (ORGN_URL)
  USING INDEX HS_URL001_PK
  ENABLE VALIDATE);
</code></pre>
* 시퀀스 생성
<pre><code>
CREATE SEQUENCE SEQ_URL
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;
</code></pre>
* DBUtil.java 의 접속정보 수정 (dbId, dbPw, dbUrl)

* 해당 프로젝트를 tomcat에 deploy 

<pre><code>
<Context docBase="파일경로/shorteningURL" path="" reloadable="true" /></Host>
</code></pre>

* localhost:8080 접속 후 화면상에서 실제 url 입력 후 변환

* 변환된 주소로 접속
