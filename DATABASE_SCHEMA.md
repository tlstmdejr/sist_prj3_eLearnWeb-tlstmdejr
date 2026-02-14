# 데이터베이스 스키마 및 테스트 데이터 문서

> 이 문서는 `intlearn_05_njw.sql` 파일을 기반으로 작성된 데이터베이스 스키마와 샘플 데이터 구조를 상세히 설명합니다.

---

## 목차
1. [데이터베이스 개요](#데이터베이스-개요)
2. [시퀀스 목록](#시퀀스-목록)
3. [테이블 구조](#테이블-구조)
4. [샘플 데이터](#샘플-데이터)
5. [데이터 관계도](#데이터-관계도)
6. [비즈니스 로직](#비즈니스-로직)

---

## 데이터베이스 개요

이 프로젝트는 온라인 학습 플랫폼(e-Learning)을 위한 데이터베이스로, 다음과 같은 주요 기능을 지원합니다:

- **사용자 관리**: 일반 사용자, 강사, 관리자 계정 관리
- **강의 관리**: 강의 등록, 카테고리 분류, 챕터 관리
- **수강 관리**: 강의 수강, 진도율 추적, 출석 관리
- **결제 관리**: 장바구니, 결제 처리, 결제 내역
- **학습 지원**: 질문/답변, 리뷰, 시험/테스트
- **운영 관리**: 공지사항, 전송 이력, 강사 승인

---

## 시퀀스 목록

데이터베이스에서 사용되는 모든 시퀀스 목록:

| 시퀀스 이름 | 용도 | 시작값 |
|------------|------|--------|
| `SEQ_MANAGER_ID` | 관리자 ID 생성 | 1 |
| `SEQ_CATEGORY_ID` | 카테고리 ID 생성 | 1 |
| `SEQ_INSTRUCTOR_ID` | 강사 ID 생성 | 1 |
| `SEQ_USERS_ID` | 사용자 ID 생성 | 1 |
| `SEQ_LECTURE_ID` | 강의 ID 생성 | 1 |
| `SEQ_RELATIVE_SKILL_ID` | 관련 기술 ID 생성 | 1 |
| `SEQ_CHAPTER_ID` | 챕터 ID 생성 | 1 |
| `SEQ_MY_CART_ID` | 장바구니 ID 생성 | 1 |
| `SEQ_PAYMENT_DETAIL_ID` | 결제 상세 ID 생성 | 1 |
| `SEQ_PAYMENT_ID` | 결제 ID 생성 | 1 |
| `SEQ_MY_LECTURE_ID` | 내 강의 ID 생성 | 1 |
| `SEQ_MY_CHAPTER_ID` | 수강 챕터 ID 생성 | 1 |
| `SEQ_QUESTION_ID` | 질문 ID 생성 | 1 |
| `SEQ_REVIEW_ID` | 리뷰 ID 생성 | 1 |
| `SEQ_TEST_ID` | 시험 ID 생성 | 1 |
| `SEQ_TEST_QUESTION_ID` | 시험 문제 ID 생성 | 1 |
| `SEQ_MY_TEST_ID` | 응시 내역 ID 생성 | 1 |
| `SEQ_ATTENDANCE_ID` | 출석 ID 생성 | 1 |
| `SEQ_ANNOUNCEMENT_ID` | 공지사항 ID 생성 | 1 |
| `SEQ_TRANS_ID` | 전송 이력 ID 생성 | 1 |

---

## 테이블 구조

### 1. 관리자 및 사용자 테이블

#### MANAGER (관리자)
```sql
CREATE TABLE MANAGER (
    ADM_ID VARCHAR2(30) PRIMARY KEY,    -- 관리자 ID
    PASSWORD VARCHAR2(20) NOT NULL      -- 비밀번호
);
```

**특징:**
- 관리자 계정 관리
- ID 형식: `admin1`, `admin2`, ...
- 샘플 데이터: 10개 계정 (admin1~admin10)

#### USERS (일반 사용자)
```sql
CREATE TABLE USERS (
    USER_ID VARCHAR2(30) PRIMARY KEY,       -- 사용자 ID
    PASSWORD VARCHAR2(100) NOT NULL,        -- 비밀번호 (BCrypt 암호화)
    EMAIL VARCHAR2(100) NOT NULL,           -- 이메일
    INTRO VARCHAR2(1000),                   -- 자기소개
    NAME VARCHAR2(100) NOT NULL,            -- 이름
    PHONE VARCHAR2(11) NOT NULL,            -- 전화번호
    ACTIVATION NUMBER(1) NOT NULL,          -- 활성화 상태 (0:비활성, 1:활성)
    BIRTH DATE NOT NULL,                    -- 생년월일
    PROFILE VARCHAR2(30) NOT NULL,          -- 프로필 이미지 경로
    REGDATE DATE DEFAULT SYSDATE NOT NULL,  -- 가입일
    REGIP VARCHAR2(30) NOT NULL             -- 가입 IP
);
```

**특징:**
- ID 형식: `user1`, `user2`, ...
- 샘플 데이터: 10개 계정
- `user10`은 비활성 상태 (ACTIVATION = 0)
- 비밀번호는 실제로는 BCrypt로 암호화되어 저장

#### INSTRUCTOR (강사)
```sql
CREATE TABLE INSTRUCTOR (
    INST_ID VARCHAR2(30) PRIMARY KEY,       -- 강사 ID
    PASSWORD VARCHAR2(100) NOT NULL,        -- 비밀번호
    BIRTH DATE NOT NULL,                    -- 생년월일
    EMAIL VARCHAR2(100) NOT NULL,           -- 이메일
    NAME VARCHAR2(100) NOT NULL,            -- 이름
    PHONE VARCHAR2(11) NOT NULL,            -- 전화번호
    PROFILE VARCHAR2(30) NOT NULL,          -- 프로필 이미지
    ACTIVATION NUMBER(1) NOT NULL,          -- 활성화 상태
    APPROVAL NUMBER(1) NOT NULL,            -- 승인 상태 (0:대기, 1:승인)
    REGDATE DATE DEFAULT SYSDATE NOT NULL,  -- 가입일
    REGIP VARCHAR2(30) NOT NULL             -- 가입 IP
);
```

**특징:**
- ID 형식: `inst1`, `inst2`, ...
- 샘플 데이터: 10개 계정
- `inst9`는 비활성 상태 (ACTIVATION = 0)
- 관리자 승인이 필요한 구조

### 2. 강의 관련 테이블

#### CATEGORY (카테고리)
```sql
CREATE TABLE CATEGORY (
    CAT_ID VARCHAR2(30) PRIMARY KEY,    -- 카테고리 ID
    NAME VARCHAR2(50) NOT NULL          -- 카테고리 이름
);
```

**샘플 카테고리 (10개):**
1. 데이터베이스
2. 웹 프로그래밍
3. 인공지능
4. 모바일 앱
5. 게임 개발
6. 보안
7. 데이터 분석
8. 클라우드
9. 디자인
10. 교양

#### LECTURE (강의)
```sql
CREATE TABLE LECTURE (
    LECT_ID VARCHAR2(30) PRIMARY KEY,       -- 강의 ID
    NAME VARCHAR2(100) NOT NULL,            -- 강의명
    PRICE NUMBER(6) NOT NULL,               -- 가격
    SHORTINT VARCHAR2(100) NOT NULL,        -- 짧은 소개
    INTRO CLOB NOT NULL,                    -- 상세 소개
    COUNT NUMBER(2) NOT NULL,               -- 챕터 수
    LENGTH NUMBER(3) NOT NULL,              -- 총 강의 시간(분)
    AVAILABILITY NUMBER(1) NOT NULL,        -- 공개 여부 (0:비공개, 1:공개)
    CAT_ID VARCHAR2(30),                    -- 카테고리 ID (FK)
    INST_ID VARCHAR2(30),                   -- 강사 ID (FK)
    THUMBNAIL VARCHAR2(30) NOT NULL,        -- 썸네일 이미지
    USERCOUNT NUMBER(5) NOT NULL,           -- 수강생 수
    REGDATE DATE DEFAULT SYSDATE NOT NULL,  -- 등록일
    REGIP VARCHAR2(30) NOT NULL,            -- 등록 IP
    APPROVAL NUMBER(1) NOT NULL,            -- 승인 상태 (0:거절, 1:승인)
    REJECT_REASON VARCHAR2(1000),           -- 거절 사유
    FOREIGN KEY (CAT_ID) REFERENCES CATEGORY(CAT_ID),
    FOREIGN KEY (INST_ID) REFERENCES INSTRUCTOR(INST_ID)
);
```

**샘플 강의 (10개):**
1. SQL 입문 (30,000원) - 데이터베이스
2. Java 마스터 (50,000원) - 웹 프로그래밍
3. AI 첫걸음 (40,000원) - 인공지능
4. 앱 개발 (55,000원) - 모바일 앱
5. Unity 게임 (60,000원) - 게임 개발 (**승인 거절 상태**)
6. 정보보안 (45,000원) - 보안
7. 데이터 분석 (50,000원) - 데이터 분석
8. AWS 클라우드 (80,000원) - 클라우드
9. UI 디자인 (35,000원) - 디자인 (**비공개 상태**)
10. 교양 철학 (20,000원) - 교양

#### RELATIVE_SKILL (관련 기술)
```sql
CREATE TABLE RELATIVE_SKILL (
    SKILL_ID NUMBER(4) PRIMARY KEY,     -- 기술 ID
    NAME VARCHAR2(50) NOT NULL,         -- 기술명
    LECT_ID VARCHAR2(30),               -- 강의 ID (FK)
    FOREIGN KEY (LECT_ID) REFERENCES LECTURE(LECT_ID)
);
```

**각 강의별 관련 기술:**
- L1: Oracle SQL
- L2: Java
- L3: Python
- L4: Flutter
- L5: C#
- L6: Network
- L7: R
- L8: AWS
- L9: Figma
- L10: Logic

#### CHAPTER (챕터)
```sql
CREATE TABLE CHAPTER (
    CHPTR_ID VARCHAR2(30) PRIMARY KEY,      -- 챕터 ID
    NUM NUMBER(2) NOT NULL,                 -- 챕터 번호
    NAME VARCHAR2(50) NOT NULL,             -- 챕터명
    LENGTH NUMBER(3) NOT NULL,              -- 재생 시간(분)
    DOC VARCHAR2(30),                       -- 자료 파일명
    DOCREGDATE DATE,                        -- 자료 등록일
    VIDEO VARCHAR2(100) NOT NULL,           -- 동영상 URL
    REGDATE DATE DEFAULT SYSDATE NOT NULL,  -- 등록일
    LECT_ID VARCHAR2(30) NOT NULL,          -- 강의 ID (FK)
    FOREIGN KEY (LECT_ID) REFERENCES LECTURE(LECT_ID)
);
```

**특징:**
- 각 강의당 1개의 기본 챕터 + 추가 챕터
- L1, L2, L3 강의에는 각각 3개의 추가 챕터가 있음
- 총 19개의 챕터 데이터

### 3. 수강 및 학습 관리 테이블

#### MY_LECTURE (내 강의)
```sql
CREATE TABLE MY_LECTURE (
    MYLECT_ID VARCHAR2(30) PRIMARY KEY,     -- 내 강의 ID
    LECT_ID VARCHAR2(30),                   -- 강의 ID (FK)
    USER_ID VARCHAR2(30),                   -- 사용자 ID (FK)
    REGDATE DATE DEFAULT SYSDATE,           -- 등록일
    FOREIGN KEY (LECT_ID) REFERENCES LECTURE(LECT_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
```

**특징:**
- 결제 완료(STATE = 1)된 강의만 자동으로 등록됨
- 중복 방지: 이미 수강 중인 강의는 장바구니에서 자동 삭제

#### MY_CHAPTER (수강 챕터)
```sql
CREATE TABLE MY_CHAPTER (
    MY_CHAPTER_ID VARCHAR2(30) PRIMARY KEY, -- 수강 챕터 ID
    CHPTR_ID VARCHAR2(30),                  -- 챕터 ID (FK)
    USER_ID VARCHAR2(30),                   -- 사용자 ID (FK)
    LASTDATE DATE,                          -- 마지막 수강일
    PROGRESS NUMBER(3) NOT NULL,            -- 진도율(%)
    PROGTIME NUMBER(5) NOT NULL,            -- 수강 시간(초)
    STATE NUMBER(1) NOT NULL,               -- 완료 상태 (0:진행중, 1:완료)
    FOREIGN KEY (CHPTR_ID) REFERENCES CHAPTER(CHPTR_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
```

**특징:**
- 챕터별 진도율 및 수강 시간 추적
- 완료 여부 관리 (STATE)

#### ATTENDANCE (출석)
```sql
CREATE TABLE ATTENDANCE (
    ATTEND_ID NUMBER(4) PRIMARY KEY,        -- 출석 ID
    USER_ID VARCHAR2(30),                   -- 사용자 ID (FK)
    ATTDATE DATE DEFAULT SYSDATE NOT NULL,  -- 출석일
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
```

**샘플 데이터:**
- 각 사용자별 출석 기록 존재
- user1, user4, user6, user8, user10은 오늘 출석 (SYSDATE)
- 나머지는 과거 날짜 출석 기록

### 4. 결제 관련 테이블

#### MY_CART (장바구니)
```sql
CREATE TABLE MY_CART (
    MYCART_ID VARCHAR2(30) PRIMARY KEY,     -- 장바구니 ID
    USER_ID VARCHAR2(30),                   -- 사용자 ID (FK)
    LECT_ID VARCHAR2(30),                   -- 강의 ID (FK)
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID),
    FOREIGN KEY (LECT_ID) REFERENCES LECTURE(LECT_ID)
);
```

**샘플 데이터 패턴:**
- user1~user9: 각 2개의 강의를 장바구니에 담음
- user10: 비활성 계정으로 장바구니 없음
- L9(UI 디자인): 비공개 강의로 장바구니에 없음

#### PAYMENT_DETAIL (결제 상세)
```sql
CREATE TABLE PAYMENT_DETAIL (
    PAYREC_ID VARCHAR2(30) PRIMARY KEY,     -- 결제 상세 ID
    USER_ID VARCHAR2(30),                   -- 사용자 ID (FK)
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
```

**특징:**
- 결제 내역의 메타데이터 관리
- 사용자별로 하나의 결제 상세 ID 할당 (PD1~PD9)

#### PAYMENT (결제)
```sql
CREATE TABLE PAYMENT (
    PAY_ID VARCHAR2(30) PRIMARY KEY,        -- 결제 ID
    PAYREC_ID VARCHAR2(30) NOT NULL,        -- 결제 상세 ID (FK)
    METHOD VARCHAR2(30) NOT NULL,           -- 결제 수단 (CARD/CASH/BANK)
    TIME DATE DEFAULT SYSDATE,              -- 결제 시간
    PRICE NUMBER(7) NOT NULL,               -- 결제 금액
    STATE NUMBER(1) NOT NULL,               -- 결제 상태 (0:장바구니, 1:완료)
    FOREIGN KEY (PAYREC_ID) REFERENCES PAYMENT_DETAIL(PAYREC_ID)
);
```

**결제 상태 (STATE):**
- 0: 장바구니 상태 (결제 미완료)
- 1: 결제 완료

**샘플 결제 내역:**
- PD4 (user4): 50,000원, CASH, **장바구니 상태 (STATE=0)**
- PD7 (user7): 45,000원, BANK, **장바구니 상태 (STATE=0)**
- 나머지: 결제 완료 상태 (STATE=1)

#### PAYMENT_DETAIL2 (결제 강의 매핑)
```sql
CREATE TABLE PAYMENT_DETAIL2 (
    LECT_ID VARCHAR2(30),                   -- 강의 ID (FK)
    PAYREC_ID VARCHAR2(30),                 -- 결제 상세 ID (FK)
    PRIMARY KEY (LECT_ID, PAYREC_ID),
    FOREIGN KEY (LECT_ID) REFERENCES LECTURE(LECT_ID),
    FOREIGN KEY (PAYREC_ID) REFERENCES PAYMENT_DETAIL(PAYREC_ID)
);
```

**특징:**
- 하나의 결제에 여러 강의 포함 가능
- 각 사용자는 2개의 강의를 구매
- L9(비공개 강의)는 구매 내역에서 제외

### 5. 질문 및 리뷰 테이블

#### QUESTION (질문)
```sql
CREATE TABLE QUESTION (
    QUESTION_ID VARCHAR2(30) PRIMARY KEY,   -- 질문 ID
    LECT_ID VARCHAR2(30),                   -- 강의 ID (FK)
    USER_ID VARCHAR2(30),                   -- 사용자 ID (FK)
    TITLE VARCHAR2(100) NOT NULL,           -- 제목
    CONTENT VARCHAR2(1000) NOT NULL,        -- 내용
    VIEWS NUMBER(5) DEFAULT 0 NOT NULL,     -- 조회수
    REGDATE DATE DEFAULT SYSDATE NOT NULL,  -- 작성일
    REGIP VARCHAR2(30) NOT NULL,            -- 작성 IP
    ANS VARCHAR2(1000),                     -- 답변 (NULL 가능)
    ANSDATE DATE,                           -- 답변일
    ANSIP VARCHAR2(30),                     -- 답변 IP
    FOREIGN KEY (LECT_ID) REFERENCES LECTURE(LECT_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
```

**샘플 데이터:**
- 각 강의당 1개의 질문
- 총 10개의 질문 (Q1~Q10)
- 일부 질문은 답변 완료, 일부는 미답변
- 모든 질문의 조회수: 1500

#### REVIEW (리뷰)
```sql
CREATE TABLE REVIEW (
    REVIEW_ID VARCHAR2(30) PRIMARY KEY,     -- 리뷰 ID
    SCORE NUMBER(1) NOT NULL,               -- 평점 (1~5)
    CONTENT VARCHAR2(1000) NOT NULL,        -- 리뷰 내용
    REGDATE DATE DEFAULT SYSDATE NOT NULL,  -- 작성일
    REGIP VARCHAR2(30) NOT NULL,            -- 작성 IP
    LECT_ID VARCHAR2(30),                   -- 강의 ID (FK)
    USER_ID VARCHAR2(30),                   -- 사용자 ID (FK)
    FOREIGN KEY (LECT_ID) REFERENCES LECTURE(LECT_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
```

**샘플 리뷰 평점:**
- L1: 5점 - "좋아요"
- L2: 4점 - "유익함"
- L3: 5점 - "최고의 강의"
- L4: 3점 - "그저 그래요"
- L5: 5점 - "완벽합니다"
- L6: 4점 - "추천해요"
- L7: 5점 - "감동적인 강의"
- L8: 2점 - "어려워요"
- L9: 5점 - "실무에 도움됨"
- L10: 4점 - "괜찮네요"

### 6. 시험 관련 테이블

#### TEST (시험)
```sql
CREATE TABLE TEST (
    TEST_ID VARCHAR2(30) PRIMARY KEY,       -- 시험 ID
    LECT_ID VARCHAR2(30),                   -- 강의 ID (FK)
    FOREIGN KEY (LECT_ID) REFERENCES LECTURE(LECT_ID)
);
```

**특징:**
- 각 강의당 1개의 시험 존재 (T1~T10)

#### TEST_QUESTION (시험 문제)
```sql
CREATE TABLE TEST_QUESTION (
    TEST_Q_ID VARCHAR2(30) PRIMARY KEY,     -- 시험 문제 ID
    TEST_ID VARCHAR2(30),                   -- 시험 ID (FK)
    NUM NUMBER(1) NOT NULL,                 -- 문제 번호
    CONTENT VARCHAR2(300) NOT NULL,         -- 문제 내용
    OPT1 VARCHAR2(200) NOT NULL,            -- 선택지 1
    OPT2 VARCHAR2(200) NOT NULL,            -- 선택지 2
    OPT3 VARCHAR2(200) NOT NULL,            -- 선택지 3
    OPT4 VARCHAR2(200) NOT NULL,            -- 선택지 4
    ANS NUMBER(1) NOT NULL,                 -- 정답 (1~4)
    EXP VARCHAR2(300),                      -- 해설
    FOREIGN KEY (TEST_ID) REFERENCES TEST(TEST_ID)
);
```

**샘플 데이터:**
- T1(SQL 입문) 시험: 5개 문제
  1. SQL이란? (정답: 3)
  2. SELECT 용도? (정답: 2)
  3. DROP 용도? (정답: 4)
  4. INSERT 용도? (정답: 2)
  5. PK 특징? (정답: 1)

#### MY_TEST (시험 응시)
```sql
CREATE TABLE MY_TEST (
    MY_TEST_ID VARCHAR2(30) PRIMARY KEY,    -- 응시 ID
    TEST_ID VARCHAR2(30) NOT NULL,          -- 시험 ID (FK)
    USER_ID VARCHAR2(30) NOT NULL,          -- 사용자 ID (FK)
    SCORE NUMBER(3),                        -- 점수
    TAKEDATE DATE DEFAULT SYSDATE,          -- 응시일
    IP VARCHAR2(30),                        -- 응시 IP
    FOREIGN KEY (TEST_ID) REFERENCES TEST(TEST_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
```

**샘플 점수:**
- user1: 80점 (T1)
- user2: 100점 (T1)
- user3: 40점 (T1)
- user4: 90점 (T1)
- user5: 70점 (T1)
- user6: 60점 (T6)
- user7: 85점 (T7)
- user8: 100점 (T8)
- user9: 30점 (T9)
- user10: 95점 (T10)

#### TEST_EXAMINATION (시험 답안)
```sql
CREATE TABLE TEST_EXAMINATION (
    TEST_Q_NUM NUMBER(1),                   -- 문제 번호
    MY_TEST_ID VARCHAR2(30),                -- 응시 ID (FK)
    CHOICE NUMBER(1) NOT NULL,              -- 선택한 답 (1~4)
    PRIMARY KEY (MY_TEST_ID, TEST_Q_NUM),
    FOREIGN KEY (MY_TEST_ID) REFERENCES MY_TEST(MY_TEST_ID)
);
```

**특징:**
- 학생이 선택한 답안 저장
- MT1(user1)과 MT2(user2)의 답안이 샘플로 입력됨

### 7. 운영 관리 테이블

#### ANNOUNCEMENT (공지사항)
```sql
CREATE TABLE ANNOUNCEMENT (
    ANN_ID NUMBER(4) PRIMARY KEY,           -- 공지사항 ID
    TITLE VARCHAR2(100) NOT NULL,           -- 제목
    CONTENT VARCHAR2(1000) NOT NULL,        -- 내용
    VIEWS NUMBER(5) DEFAULT 0,              -- 조회수
    REGDATE DATE DEFAULT SYSDATE,           -- 작성일
    REGIP VARCHAR2(30) NOT NULL,            -- 작성 IP
    ADM_ID VARCHAR2(30),                    -- 관리자 ID (FK)
    FOREIGN KEY (ADM_ID) REFERENCES MANAGER(ADM_ID)
);
```

**샘플 공지사항 (10개):**
1. 점검공지 (조회수: 90)
2. 이벤트 (조회수: 155)
3. 업데이트 (조회수: 999)
4. 공지사항 (조회수: 8766)
5. 안내 (조회수: 4676)
6. 긴급 (조회수: 125)
7. 이벤트 종료 (조회수: 12)
8. 필독 (조회수: 455)
9. 점검 완료 (조회수: 22)
10. 기타 (조회수: 15)

#### TRANSMISSION_HISTORY (전송 이력)
```sql
CREATE TABLE TRANSMISSION_HISTORY (
    TRANS_ID NUMBER(4) PRIMARY KEY,         -- 전송 ID
    PHONE VARCHAR2(11),                     -- 전화번호
    EMAIL VARCHAR2(30),                     -- 이메일
    TRANSDATE DATE DEFAULT SYSDATE NOT NULL -- 전송일
);
```

**특징:**
- SMS/이메일 전송 이력 추적
- 10개의 샘플 전송 이력 존재

---

## 샘플 데이터

### 계정 정보 요약

#### 관리자 (10명)
| ID | 비밀번호 |
|----|---------|
| admin1 | pass01 |
| admin2 | pass02 |
| ... | ... |
| admin10 | pass10 |

#### 사용자 (10명)
| ID | 이름 | 이메일 | 자기소개 | 상태 |
|----|------|--------|----------|------|
| user1 | 이정우 | u1@naver.com | 멋쟁이 | 활성 |
| user2 | 남지우 | u2@naver.com | 진짜멋쟁이 | 활성 |
| user3 | 이지원 | u3@naver.com | 레알멋쟁이 | 활성 |
| user4 | 신승덕 | u4@naver.com | 자바를 좋아하는 학생 | 활성 |
| user5 | 최승준 | u5@naver.com | 노캡으로 멋쟁이 | 활성 |
| user6 | 버즈 | u6@naver.com | 겁쟁이 | 활성 |
| user7 | 나경원 | u7@naver.com | 집가고싶은 학생 | 활성 |
| user8 | 윤자빈 | u8@naver.com | 공부하기싫은 학생 | 활성 |
| user9 | 박상현 | u9@naver.com | 생물학적 남성 | 활성 |
| user10 | 고준서 | u10@naver.com | 생물학적 중성 | **비활성** |

#### 강사 (10명)
| ID | 이름 | 이메일 | 상태 | 승인 |
|----|------|--------|------|------|
| inst1 | 강사1 | i1@test.com | 활성 | 승인 |
| inst2 | 강사2 | i2@test.com | 활성 | 승인 |
| ... | ... | ... | ... | ... |
| inst9 | 강사9 | i9@test.com | **비활성** | 승인 |
| inst10 | 강사10 | i10@test.com | 활성 | 승인 |

### 강의 정보 요약

| 강의 ID | 강의명 | 가격 | 카테고리 | 강사 | 공개 | 승인 | 특이사항 |
|--------|--------|------|----------|------|------|------|----------|
| L1 | SQL 입문 | 30,000 | 데이터베이스 | inst1 | ✅ | ✅ | - |
| L2 | Java 마스터 | 50,000 | 웹 프로그래밍 | inst2 | ✅ | ✅ | - |
| L3 | AI 첫걸음 | 40,000 | 인공지능 | inst3 | ✅ | ✅ | - |
| L4 | 앱 개발 | 55,000 | 모바일 앱 | inst4 | ✅ | ✅ | - |
| L5 | Unity 게임 | 60,000 | 게임 개발 | inst5 | ✅ | ❌ | 거절(비싸서 거절) |
| L6 | 정보보안 | 45,000 | 보안 | inst6 | ✅ | ✅ | - |
| L7 | 데이터 분석 | 50,000 | 데이터 분석 | inst7 | ✅ | ✅ | - |
| L8 | AWS 클라우드 | 80,000 | 클라우드 | inst8 | ✅ | ✅ | - |
| L9 | UI 디자인 | 35,000 | 디자인 | inst9 | ❌ | ✅ | 비공개 |
| L10 | 교양 철학 | 20,000 | 교양 | inst10 | ✅ | ✅ | - |

---

## 데이터 관계도

### 주요 관계 구조

```
MANAGER (관리자)
    └─> ANNOUNCEMENT (공지사항 작성)

INSTRUCTOR (강사)
    └─> LECTURE (강의 등록)
            ├─> CATEGORY (카테고리 분류)
            ├─> RELATIVE_SKILL (관련 기술)
            ├─> CHAPTER (챕터)
            ├─> TEST (시험)
            │    └─> TEST_QUESTION (시험 문제)
            ├─> QUESTION (질문)
            └─> REVIEW (리뷰)

USERS (사용자)
    ├─> MY_CART (장바구니)
    │    └─> LECTURE (담은 강의)
    ├─> PAYMENT_DETAIL (결제 상세)
    │    └─> PAYMENT (결제)
    │         └─> PAYMENT_DETAIL2 (구매한 강의들)
    ├─> MY_LECTURE (수강 중인 강의)
    │    └─> LECTURE
    ├─> MY_CHAPTER (수강 챕터)
    │    └─> CHAPTER
    ├─> MY_TEST (시험 응시)
    │    ├─> TEST
    │    └─> TEST_EXAMINATION (답안)
    ├─> QUESTION (질문 작성)
    ├─> REVIEW (리뷰 작성)
    └─> ATTENDANCE (출석)

TRANSMISSION_HISTORY (전송 이력) - 독립적
```

---

## 비즈니스 로직

### 1. 결제 프로세스

```sql
-- 결제 완료 시 자동으로 내 강의실에 등록
INSERT INTO MY_LECTURE (MYLECT_ID, LECT_ID, USER_ID, REGDATE)
SELECT 'ML' || SEQ_MY_LECTURE_ID.NEXTVAL, pd2.LECT_ID, pd.USER_ID, SYSDATE
FROM PAYMENT_DETAIL2 pd2
JOIN PAYMENT_DETAIL pd ON pd2.PAYREC_ID = pd.PAYREC_ID
JOIN PAYMENT p ON pd.PAYREC_ID = p.PAYREC_ID
WHERE p.STATE = 1;  -- 결제 완료된 것만
```

### 2. 장바구니 중복 방지

```sql
-- 이미 수강 중인 강의는 장바구니에서 자동 삭제
DELETE FROM MY_CART
WHERE (USER_ID, LECT_ID) IN (
    SELECT USER_ID, LECT_ID FROM MY_LECTURE
);
```

### 3. 강사별 매출 조회 쿼리

```sql
SELECT
    i.NAME AS "강사명",
    l.NAME AS "강의명",
    COUNT(CASE WHEN p.STATE = 2 THEN 1 END) AS "결제건수(수강생수)",
    SUM(CASE WHEN p.STATE = 2 THEN l.PRICE ELSE 0 END) AS "총매출액"
FROM LECTURE l
JOIN INSTRUCTOR i ON l.INST_ID = i.INST_ID
LEFT JOIN PAYMENT_DETAIL2 pd2 ON l.LECT_ID = pd2.LECT_ID
LEFT JOIN PAYMENT p ON pd2.PAYREC_ID = p.PAYREC_ID
GROUP BY i.NAME, l.NAME
ORDER BY i.NAME, l.NAME;
```

### 4. 사용자별 장바구니 조회

```sql
SELECT
    u.NAME AS "사용자명",
    u.USER_ID AS "아이디",
    l.NAME AS "장바구니에_담은_강의",
    l.PRICE AS "가격",
    i.NAME AS "강사명"
FROM MY_CART c
JOIN USERS u ON c.USER_ID = u.USER_ID
JOIN LECTURE l ON c.LECT_ID = l.LECT_ID
JOIN INSTRUCTOR i ON l.INST_ID = i.INST_ID
ORDER BY u.USER_ID, l.NAME;
```

---

## 주요 제약사항 및 규칙

### 1. 계정 상태 관리
- **ACTIVATION**: 0(비활성) / 1(활성)
- **APPROVAL** (강사만): 0(승인 대기) / 1(승인 완료)
- 비활성 계정: user10, inst9

### 2. 강의 상태 관리
- **AVAILABILITY**: 0(비공개) / 1(공개)
- **APPROVAL**: 0(거절) / 1(승인)
- 비공개 강의: L9 (UI 디자인)
- 승인 거절: L5 (Unity 게임) - 사유: "비싸서 거절"

### 3. 결제 상태
- **STATE**: 0(장바구니) / 1(결제 완료)
- 장바구니 상태: PD4(user4), PD7(user7)

### 4. 챕터 완료 상태
- **STATE**: 0(진행 중) / 1(완료)
- **PROGRESS**: 진도율 (0~100%)

### 5. ID 명명 규칙
- 관리자: `admin1`, `admin2`, ...
- 사용자: `user1`, `user2`, ...
- 강사: `inst1`, `inst2`, ...
- 강의: `L1`, `L2`, ...
- 카테고리: `CAT1`, `CAT2`, ...
- 챕터: `CH1`, `CH2`, ...
- 장바구니: `CART1`, `CART2`, ...
- 결제 상세: `PD1`, `PD2`, ...
- 결제: `P1`, `P2`, ...
- 질문: `Q1`, `Q2`, ...
- 리뷰: `R1`, `R2`, ...
- 시험: `T1`, `T2`, ...
- 시험 문제: `TQ1`, `TQ2`, ...
- 응시: `MT1`, `MT2`, ...

---

## 참고 사항

### 데이터 초기화
SQL 파일은 다음 순서로 실행됩니다:
1. 기존 시퀀스 및 테이블 삭제
2. 시퀀스 생성
3. 테이블 생성
4. 샘플 데이터 삽입
5. COMMIT

### 테스트 시나리오
이 샘플 데이터는 다음과 같은 테스트 시나리오를 지원합니다:
- ✅ 정상 사용자 로그인 및 수강
- ✅ 비활성 계정 로그인 차단
- ✅ 강사 승인 프로세스
- ✅ 장바구니 및 결제 프로세스
- ✅ 강의 승인/거절 프로세스
- ✅ 질문/답변 기능
- ✅ 리뷰 및 평점 시스템
- ✅ 시험 응시 및 채점
- ✅ 진도율 추적
- ✅ 출석 관리

---

## 문서 버전 정보

- **작성일**: 2026-02-11
- **기반 파일**: `intlearn_05_njw.sql`
- **총 테이블 수**: 23개
- **총 시퀀스 수**: 20개
- **샘플 사용자 수**: 10명 (일반 사용자)
- **샘플 강사 수**: 10명
- **샘플 관리자 수**: 10명
- **샘플 강의 수**: 10개
