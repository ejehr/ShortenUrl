[�Ұ�]
8�ڸ� ���� ����URL ����
url ��ȣȭ (sha256) 3�ڸ�+ DB ������ base62��ȯ�� �̿��Ͽ� url�� ��ȯ
DB���̺� ������ ��ȯ�� �ּҿ� �ߺ�üũ ��
���� ��ȯ�� �ּҰ� �����ÿ��� redirect ��

[����ȯ��]
java 1.8
tomcat 7.0
oracle 11g

[������]

* db ���̺� ���� 

DROP TABLE HS_URL_001 CASCADE CONSTRAINTS;

CREATE TABLE HS_URL_001
(
  ORGN_URL     VARCHAR2(4000 BYTE)              NOT NULL,
  SEQ_NO       NUMBER,
  SHORTEN_URL  VARCHAR2(4000 BYTE),
  INS_YMDHMS   TIMESTAMP(6) WITH TIME ZONE
);

COMMENT ON TABLE HS_URL_001 IS '(HS_URL_001) URL���';

COMMENT ON COLUMN HS_URL_001.ORGN_URL IS '����URL';

COMMENT ON COLUMN HS_URL_001.SEQ_NO IS '�Ϸù�ȣ';

COMMENT ON COLUMN HS_URL_001.SHORTEN_URL IS '����URL';

COMMENT ON COLUMN HS_URL_001.INS_YMDHMS IS '�Է��Ͻ�';

CREATE UNIQUE INDEX HS_URL_001_PK ON HS_URL_001
(ORGN_URL, SEQ_NO);

ALTER TABLE HS_URL_001 ADD (
  CONSTRAINT HS_URL001_PK
  PRIMARY KEY
  (ORGN_URL)
  USING INDEX HS_URL001_PK
  ENABLE VALIDATE);

* ������ ����
CREATE SEQUENCE SEQ_URL
  START WITH 79
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;

* DBUtil.java �� �������� ���� (dbId, dbPw, dbUrl)

* �ش� ������Ʈ�� tomcat�� deploy �� localhost:��Ʈ ����

* ȭ��󿡼� ���� url �Է� �� ��ȯ

* ��ȯ�� �ּҷ� ����
