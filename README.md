# sist_prj3_eLearnWeb - 온라인 학습 플랫폼

> Spring Boot + MyBatis 기반 온라인 강의 플랫폼

---

## 📚 프로젝트 개요

이 프로젝트는 온라인 학습 플랫폼(e-Learning)으로, 강사가 강의를 등록하고 학생들이 수강할 수 있는 종합 교육 시스템입니다.

### 주요 기능
- 👥 **사용자 관리**: 일반 사용자, 강사, 관리자 계정 관리
- 📖 **강의 관리**: 강의 등록, 카테고리 분류, 챕터별 학습
- 🎓 **수강 관리**: 강의 수강, 진도율 추적, 출석 체크
- 💳 **결제 시스템**: 장바구니, 다양한 결제 수단 지원
- 💬 **학습 지원**: 질문/답변, 리뷰 작성, 시험/테스트
- 🔔 **운영 관리**: 공지사항, 강사 승인 프로세스

---

## 📖 문서 목록

### 핵심 문서
- **[DATABASE_SCHEMA.md](./DATABASE_SCHEMA.md)** - 데이터베이스 스키마 및 테스트 데이터 상세 문서
  - 전체 테이블 구조 및 관계
  - 샘플 데이터 설명
  - 비즈니스 로직 SQL 쿼리
  - ID 명명 규칙 및 제약사항

- **[SQL_SCHEMA_ALIGNMENT.md](./SQL_SCHEMA_ALIGNMENT.md)** ⭐ NEW! - SQL 스키마 정렬 작업 문서
  - 실제 DB와 프론트엔드 코드 간 불일치 사항 분석
  - Before/After 비교
  - 테이블/컬럼 매핑표
  - VO 클래스 구현 가이드

- **[project-overview.md](./project-overview.md)** - 프로젝트 전체 기능 및 워크플로우
  - 컨트롤러별 URL 매핑
  - 기능별 워크플로우
  - 템플릿 및 Mapper 구조

### 기술 분석 문서
- **[Spring_MyBatis_Login_Structure_Review.md](./Spring_MyBatis_Login_Structure_Review.md)** - 로그인 구조 분석
- **[Spring_Security_Encryption_Analysis.md](./Spring_Security_Encryption_Analysis.md)** - 암호화 구조 분석

### 개선 문서
- **[MAIN_PAGE_IMPROVEMENTS.md](./MAIN_PAGE_IMPROVEMENTS.md)** - 메인 페이지 개선 사항
- **[BEFORE_AFTER_COMPARISON.md](./BEFORE_AFTER_COMPARISON.md)** - 개선 전후 비교

---

## 🗄️ 데이터베이스

### 초기화 스크립트
```bash
# Oracle SQL Developer 또는 SQL*Plus에서 실행
@intlearn_05_njw.sql
```

### 주요 테이블 (23개)
- **계정 관리**: MANAGER, USERS, INSTRUCTOR
- **강의 관리**: CATEGORY, LECTURE, CHAPTER, RELATIVE_SKILL
- **수강 관리**: MY_LECTURE, MY_CHAPTER, ATTENDANCE
- **결제 관리**: MY_CART, PAYMENT, PAYMENT_DETAIL, PAYMENT_DETAIL2
- **학습 지원**: QUESTION, REVIEW, TEST, TEST_QUESTION, MY_TEST, TEST_EXAMINATION
- **운영 관리**: ANNOUNCEMENT, TRANSMISSION_HISTORY

자세한 내용은 **[DATABASE_SCHEMA.md](./DATABASE_SCHEMA.md)** 참조

---

## 🛠️ 기술 스택

### Backend
- **Framework**: Spring Boot 3.x
- **ORM**: MyBatis
- **Database**: Oracle Database
- **Build Tool**: Maven
- **Template Engine**: Thymeleaf

### Security
- **Password**: BCrypt (단방향 해시)
- **Data Encryption**: AES (양방향 암호화)
- **Session**: Spring Session

### External Services
- **SMS**: Solapi API
- **Email**: Gmail SMTP

---

## 📂 프로젝트 구조

```
kr.co.sist
├── common/          # 공통 기능 (아이디/비밀번호 찾기, 이메일 서비스)
├── admin/           # 관리자 기능 (회원관리, 강사승인)
├── instructor/      # 강사 기능 (회원가입, 로그인, 대시보드)
├── user/            # 유저 기능 (회원가입, 로그인, 프로필, 설정)
└── config/          # 설정 (WebMvcConfig)
```

---

## 🚀 시작하기

### 1. 데이터베이스 설정
```sql
-- Oracle Database에 접속하여 실행
@intlearn_05_njw.sql
```

### 2. 애플리케이션 설정
```bash
# application-dev.properties 설정
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. 애플리케이션 실행
```bash
./mvnw spring-boot:run
```

### 4. 접속
```
http://localhost:8080
```

---

## 👥 사용자 역할

### 일반 사용자
- 강의 검색 및 수강
- 장바구니 및 결제
- 학습 진도 관리
- 질문 작성 및 리뷰

### 강사
- 강의 등록 및 관리
- 챕터 업로드
- 질문 답변
- 수강생 관리

### 관리자
- 회원 관리
- 강사 승인
- 공지사항 작성
- 전체 통계 조회

---

## 🧪 테스트 계정

### 관리자
- ID: `admin1` ~ `admin10`
- PW: `pass01` ~ `pass10`

### 강사
- ID: `inst1` ~ `inst10`
- PW: `p1` ~ `p10`
- 주의: `inst9`는 비활성 계정

### 일반 사용자
- ID: `user1` ~ `user10`
- PW: `u1` ~ `u10`
- 주의: `user10`은 비활성 계정

자세한 테스트 데이터는 **[DATABASE_SCHEMA.md](./DATABASE_SCHEMA.md)** 참조

---

## 📋 주요 기능 워크플로우

### 강의 수강 흐름
```
1. 회원가입/로그인
2. 강의 검색 및 상세보기
3. 장바구니 담기
4. 결제 진행
5. 내 강의실에서 수강
6. 챕터별 학습 및 진도율 관리
7. 시험 응시
8. 리뷰 작성
```

### 강사 강의 등록 흐름
```
1. 강사 회원가입
2. 관리자 승인 대기
3. 승인 후 로그인
4. 강의 등록 (카테고리, 가격, 소개 등)
5. 챕터 업로드 (동영상, 자료)
6. 시험 문제 등록
7. 관리자 강의 승인
8. 강의 공개
```

---

## 🔒 보안 기능

### 비밀번호 암호화
- BCrypt 단방향 해시 사용
- Salt 자동 생성

### 개인정보 암호화
- AES-256 양방향 암호화
- 이메일, 이름 등 민감 정보 보호

### 세션 관리
- 세션 타임아웃: 30분
- 로그아웃 시 세션 완전 삭제

---

## 📞 고객센터 기능

### 질문/답변 (Q&A)
- 강의별 질문 게시판
- 강사가 직접 답변
- 조회수 추적

### 공지사항
- 관리자가 작성
- 중요 공지 상단 고정 가능
- 조회수 추적

---

## 📊 통계 및 리포트

### 강사 대시보드
- 내 강의 수강생 수
- 강의별 매출액
- 질문 답변 현황
- 평균 평점

### 관리자 대시보드
- 전체 회원 수 (사용자/강사)
- 강의 승인 대기 목록
- 일일 매출 통계
- 인기 강의 순위

---

## 🛠️ 수정 예정 사항

`수정할일.txt` 참조:
1. 프로필 이미지 업로드 개선
2. 이메일 인증 기능 모달로 구현
3. 휴대전화 인증 중복 방지
4. 자기소개 에디터 교체 (SummerNote/SmartEditor)
5. 세션 무효화 개선
6. 계정 탈퇴 기능 구현
7. 강사/관리자 로그인 테스트 페이지 생성

---

## 📝 라이센스

이 프로젝트는 교육 목적으로 제작되었습니다.

---

## 👨‍💻 개발자

- GitHub: [tlstmdejr](https://github.com/tlstmdejr)

---

## 📅 버전 정보

- **버전**: 1.0.0
- **최종 업데이트**: 2026-02-11
- **Spring Boot**: 3.x
- **Java**: 17+
- **Database**: Oracle 11g 이상

---

## 🔗 관련 링크

- [프로젝트 상세 기능](./project-overview.md)
- [데이터베이스 스키마](./DATABASE_SCHEMA.md)
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [MyBatis 공식 문서](https://mybatis.org/mybatis-3/)
