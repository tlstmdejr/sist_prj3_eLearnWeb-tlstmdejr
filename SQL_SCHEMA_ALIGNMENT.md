# SQL 스키마 정렬 작업 (SQL Schema Alignment)

> 작성일: 2026-02-11  
> 작업 내용: intlearn_05_njw.sql 분석을 기반으로 메인 페이지 문서 및 데이터 구조 수정

---

## 📋 작업 개요

`DATABASE_SCHEMA.md`에 문서화된 실제 데이터베이스 스키마(`intlearn_05_njw.sql`)를 기반으로 메인 페이지 관련 문서 및 HTML 파일의 데이터 구조를 실제 DB와 일치하도록 수정했습니다.

---

## 🔍 발견된 불일치 사항

### 1. 카테고리 데이터 불일치

**문제점:**
- HTML에는 5개의 일반적인 카테고리만 정의됨
- 실제 DB에는 10개의 구체적인 카테고리 존재

**Before (HTML - index.html):**
```javascript
const CATEGORIES = [
    { id: 'all', name: '전체', icon: null },
    { id: 'dev', name: '개발 · 프로그래밍', icon: 'code' },
    { id: 'security', name: '보안 · 네트워크', icon: 'shield' },
    { id: 'data', name: '데이터 사이언스', icon: 'database' },
    { id: 'mobile', name: '모바일', icon: 'smartphone' },
    { id: 'game', name: '게임 개발', icon: 'monitor' },
];
```

**After (실제 DB 구조 반영):**
```javascript
const CATEGORIES = [
    { id: 'all', name: '전체', icon: null },
    { id: 'CAT1', name: '데이터베이스', icon: 'database' },
    { id: 'CAT2', name: '웹 프로그래밍', icon: 'code' },
    { id: 'CAT3', name: '인공지능', icon: 'brain' },
    { id: 'CAT4', name: '모바일 앱', icon: 'smartphone' },
    { id: 'CAT5', name: '게임 개발', icon: 'gamepad' },
    { id: 'CAT6', name: '보안', icon: 'shield' },
    { id: 'CAT7', name: '데이터 분석', icon: 'bar-chart' },
    { id: 'CAT8', name: '클라우드', icon: 'cloud' },
    { id: 'CAT9', name: '디자인', icon: 'palette' },
    { id: 'CAT10', name: '교양', icon: 'book-open' },
];
```

**실제 DB (CATEGORY 테이블):**
```sql
INSERT INTO CATEGORY VALUES ('CAT1', '데이터베이스');
INSERT INTO CATEGORY VALUES ('CAT2', '웹 프로그래밍');
INSERT INTO CATEGORY VALUES ('CAT3', '인공지능');
INSERT INTO CATEGORY VALUES ('CAT4', '모바일 앱');
INSERT INTO CATEGORY VALUES ('CAT5', '게임 개발');
INSERT INTO CATEGORY VALUES ('CAT6', '보안');
INSERT INTO CATEGORY VALUES ('CAT7', '데이터 분석');
INSERT INTO CATEGORY VALUES ('CAT8', '클라우드');
INSERT INTO CATEGORY VALUES ('CAT9', '디자인');
INSERT INTO CATEGORY VALUES ('CAT10', '교양');
```

### 2. 강의 데이터 불일치

**문제점:**
- HTML의 샘플 강의 데이터가 실제 DB 구조와 다름
- 8개의 임의 강의 vs 10개의 실제 샘플 강의
- 카테고리 ID, 가격, 강사명이 실제 데이터와 불일치

**Before (HTML - 8개 임의 데이터):**
```javascript
const BASE_COURSES = [
    { 
        id: 1, 
        title: "[풀스택] 스프링 부트와 리액트로 만드는 쇼핑몰", 
        instructor: "김코딩", 
        price: 121000,
        category: "dev",  // ❌ 실제 DB에는 "CAT1", "CAT2" 형식
        // ...
    },
    // ... 8개
];
```

**After (실제 DB 샘플 데이터 반영):**
```javascript
const BASE_COURSES = [
    { 
        id: 1, 
        title: "SQL 입문", 
        instructor: "강사1", 
        price: 30000,
        category: "CAT1",  // ✅ 실제 DB와 일치
        // ...
    },
    // ... 10개 (L1~L10 실제 샘플 데이터)
];
```

**실제 DB (LECTURE 테이블):**
```sql
INSERT INTO LECTURE VALUES ('L1', 'SQL 입문', 30000, '기초부터', '강의설명1', 10, 100, 1, 'CAT1', 'inst1', ...);
INSERT INTO LECTURE VALUES ('L2', 'Java 마스터', 50000, '심화과정', '강의설명2', 12, 120, 1, 'CAT2', 'inst2', ...);
-- ... L3~L10
```

### 3. 테이블 명명 불일치

**문제점:**
- 문서에 일반적인 테이블명 사용 (courses, categories)
- 실제 DB는 대문자 명명 규칙 사용 (LECTURE, CATEGORY, INSTRUCTOR)

**Before (MAIN_PAGE_IMPROVEMENTS.md):**
```sql
CREATE TABLE categories (
    category_id VARCHAR2(20) PRIMARY KEY,
    category_name VARCHAR2(50) NOT NULL,
    ...
);

CREATE TABLE courses (
    course_id NUMBER PRIMARY KEY,
    title VARCHAR2(200) NOT NULL,
    instructor_name VARCHAR2(50),
    ...
);
```

**After (실제 스키마 반영):**
```sql
-- 실제 테이블: CATEGORY
CREATE TABLE CATEGORY (
    CAT_ID VARCHAR2(30) PRIMARY KEY,
    NAME VARCHAR2(50) NOT NULL
);

-- 실제 테이블: INSTRUCTOR
CREATE TABLE INSTRUCTOR (
    INST_ID VARCHAR2(30) PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL,
    ...
);

-- 실제 테이블: LECTURE
CREATE TABLE LECTURE (
    LECT_ID VARCHAR2(30) PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL,
    PRICE NUMBER(6) NOT NULL,
    CAT_ID VARCHAR2(30),  -- FK to CATEGORY
    INST_ID VARCHAR2(30),  -- FK to INSTRUCTOR
    ...
);
```

### 4. SQL 쿼리 예시 불일치

**문제점:**
- MyBatis mapper 예시가 가상의 테이블 구조 사용
- 실제 조인이 필요한 복잡한 쿼리 구조 반영 안됨

**Before:**
```xml
<select id="selectCourseList" resultType="CourseVO">
    SELECT * FROM courses
    WHERE category = #{category}
    ORDER BY created_at DESC
</select>
```

**After (실제 DB 조인 구조 반영):**
```xml
<select id="selectCourseList" resultType="LectureVO">
    SELECT 
        l.LECT_ID as lectId,
        l.NAME as name,
        i.NAME as instructorName,
        l.PRICE as price,
        AVG(r.SCORE) as avgRating,
        COUNT(r.REVIEW_ID) as reviewCount
    FROM LECTURE l
    INNER JOIN INSTRUCTOR i ON l.INST_ID = i.INST_ID
    INNER JOIN CATEGORY c ON l.CAT_ID = c.CAT_ID
    LEFT JOIN REVIEW r ON l.LECT_ID = r.LECT_ID
    WHERE l.AVAILABILITY = 1 AND l.APPROVAL = 1
    <if test="category != null">
        AND l.CAT_ID = #{category}
    </if>
    GROUP BY l.LECT_ID, l.NAME, i.NAME, l.PRICE, l.CAT_ID, c.NAME
    ORDER BY l.REGDATE DESC
</select>
```

---

## ✅ 수정 사항 요약

### 1. MAIN_PAGE_IMPROVEMENTS.md
- ✅ 데이터베이스 테이블 설계 섹션을 실제 스키마로 대체
- ✅ CATEGORY, INSTRUCTOR, LECTURE, REVIEW 테이블 정의 추가
- ✅ 실제 샘플 데이터 (CAT1~CAT10, L1~L10) 명시
- ✅ 테이블/컬럼 매핑 표 추가
- ✅ MyBatis 쿼리를 실제 JOIN 구조로 업데이트

### 2. BEFORE_AFTER_COMPARISON.md
- ✅ Spring Boot 연동 예시를 실제 테이블명으로 업데이트
- ✅ Thymeleaf 예시에 실제 컬럼명 반영 (lecture.name, lecture.instructorName 등)
- ✅ CategoryVO, LectureVO 사용 명시

### 3. src/main/resources/templates/common/main/index.html
- ✅ CATEGORIES 배열을 10개의 실제 카테고리로 업데이트 (CAT1~CAT10)
- ✅ BASE_COURSES를 10개의 실제 샘플 강의로 교체
- ✅ 강의 데이터 구조를 실제 LECTURE 테이블 컬럼과 일치시킴
- ✅ 더미 데이터 생성 루프를 11~60으로 수정 (기본 10개 → 총 60개)
- ✅ 주석에 실제 DB 테이블명 및 SQL 쿼리 추가
- ✅ Spring/MyBatis 연동 예시를 실제 조인 쿼리로 업데이트

---

## 📊 테이블/컬럼 매핑표

| UI 필드 | HTML 변수명 | 실제 테이블 | 실제 컬럼 | 비고 |
|---------|------------|-----------|----------|------|
| 카테고리 ID | category.id | CATEGORY | CAT_ID | CAT1, CAT2, ... |
| 카테고리명 | category.name | CATEGORY | NAME | 데이터베이스, 웹 프로그래밍, ... |
| 강의 ID | course.id | LECTURE | LECT_ID | L1, L2, ... |
| 강의명 | course.title | LECTURE | NAME | SQL 입문, Java 마스터, ... |
| 강사명 | course.instructor | INSTRUCTOR | NAME | JOIN 필요 |
| 가격 | course.price | LECTURE | PRICE | 원 단위 (30000, 50000, ...) |
| 평점 | course.rating | REVIEW | AVG(SCORE) | 집계 쿼리 필요 |
| 리뷰수 | course.reviewCount | REVIEW | COUNT(*) | 집계 쿼리 필요 |
| 썸네일 | course.thumbnail | LECTURE | THUMBNAIL | t1.jpg, t2.jpg, ... |
| 강의소개 | course.description | LECTURE | INTRO | CLOB 타입 |

---

## 🎯 실제 샘플 데이터 매핑

| ID | 강의명 | 가격 | 카테고리 | 강사 | 평점 | 비고 |
|----|--------|------|----------|------|------|------|
| L1 | SQL 입문 | 30,000원 | CAT1 (데이터베이스) | 강사1 | 5 | ✅ 공개, 승인 |
| L2 | Java 마스터 | 50,000원 | CAT2 (웹 프로그래밍) | 강사2 | 4 | ✅ 공개, 승인 |
| L3 | AI 첫걸음 | 40,000원 | CAT3 (인공지능) | 강사3 | 5 | ✅ 공개, 승인 |
| L4 | 앱 개발 | 55,000원 | CAT4 (모바일 앱) | 강사4 | 3 | ✅ 공개, 승인 |
| L5 | Unity 게임 | 60,000원 | CAT5 (게임 개발) | 강사5 | 5 | ❌ 승인 거절 |
| L6 | 정보보안 | 45,000원 | CAT6 (보안) | 강사6 | 4 | ✅ 공개, 승인 |
| L7 | 데이터 분석 | 50,000원 | CAT7 (데이터 분석) | 강사7 | 5 | ✅ 공개, 승인 |
| L8 | AWS 클라우드 | 80,000원 | CAT8 (클라우드) | 강사8 | 2 | ✅ 공개, 승인 |
| L9 | UI 디자인 | 35,000원 | CAT9 (디자인) | 강사9 | 5 | ❌ 비공개 |
| L10 | 교양 철학 | 20,000원 | CAT10 (교양) | 강사10 | 4 | ✅ 공개, 승인 |

---

## 💡 구현 시 주의사항

### VO/DTO 클래스 생성 시
```java
// LectureVO.java
public class LectureVO {
    private String lectId;        // LECT_ID
    private String name;           // NAME
    private int price;             // PRICE
    private String shortInt;       // SHORTINT
    private String intro;          // INTRO (CLOB)
    private String catId;          // CAT_ID (FK)
    private String instId;         // INST_ID (FK)
    private String thumbnail;      // THUMBNAIL
    private int userCount;         // USERCOUNT
    private int availability;      // AVAILABILITY
    private int approval;          // APPROVAL
    
    // JOIN으로 가져올 데이터
    private String instructorName; // INSTRUCTOR.NAME
    private String categoryName;   // CATEGORY.NAME
    private double avgRating;      // AVG(REVIEW.SCORE)
    private int reviewCount;       // COUNT(REVIEW.REVIEW_ID)
}

// CategoryVO.java
public class CategoryVO {
    private String catId;          // CAT_ID
    private String name;           // NAME
}
```

### MyBatis ResultMap 설정
```xml
<resultMap id="LectureResultMap" type="LectureVO">
    <id property="lectId" column="LECT_ID"/>
    <result property="name" column="NAME"/>
    <result property="price" column="PRICE"/>
    <result property="instructorName" column="INSTRUCTOR_NAME"/>
    <result property="avgRating" column="AVG_RATING"/>
    <!-- ... -->
</resultMap>
```

### 쿼리 작성 시 필수 조건
```sql
WHERE l.AVAILABILITY = 1      -- 공개된 강의만
  AND l.APPROVAL = 1          -- 승인된 강의만
  AND i.ACTIVATION = 1        -- 활성 강사만
```

---

## 📖 참고 문서

- **DATABASE_SCHEMA.md**: 전체 데이터베이스 스키마 상세 문서
- **intlearn_05_njw.sql**: 실제 SQL 파일 (481 라인)
- **project-overview.md**: 프로젝트 전체 기능 및 워크플로우

---

## ✨ 작업 효과

1. **정확성 향상**: 실제 DB 구조와 완전히 일치
2. **개발 효율성**: 복사-붙여넣기로 바로 사용 가능한 SQL 쿼리
3. **유지보수성**: 문서와 코드 간 불일치 제거
4. **학습 자료**: 실제 프로젝트 구조 이해에 도움

---

## 🔄 향후 작업

1. **VO 클래스 생성**: LectureVO, CategoryVO, InstructorVO 등
2. **Mapper 구현**: 문서화된 SQL 쿼리를 실제 Mapper XML로 작성
3. **Service 레이어**: 비즈니스 로직 구현
4. **Controller 연동**: Thymeleaf 템플릿과 연동
5. **테스트**: 실제 DB 데이터로 검증

---

**작성자**: GitHub Copilot  
**검토**: DATABASE_SCHEMA.md 기반  
**상태**: ✅ 완료
