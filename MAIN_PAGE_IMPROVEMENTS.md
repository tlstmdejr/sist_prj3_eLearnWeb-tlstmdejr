# 메인 페이지 개선 문서 (Main Page Improvements)

## 작업 일자
2026-02-11

## 작업 내용 요약

### 1. 새 파일 생성
- **위치**: `src/main/resources/templates/common/main/index.html`
- **기반**: 기존 `3차 메인페이지 테스트 백업.html` 파일을 개선
- **파일 크기**: 58,470 bytes (1,308 lines)
- **원본 크기**: 45,829 bytes (832 lines)

### 2. 추가된 구조적 주석

#### 파일 헤더 주석
- 파일명, 작성일, 설명 추가
- 전체 구조 설명 (5개 주요 섹션)
- 사용된 기술 스택 명시
- Spring + MyBatis 연동 시 변경 사항 안내

#### 각 섹션별 주석
1. **Header (헤더)**
   - 역할: 사이트 네비게이션 및 사용자 인터페이스
   - 구성 요소 상세 설명
   - Spring 연동 시 Thymeleaf 사용 예시

2. **Hero Banner (메인 배너)**
   - 역할: 프로모션, 이벤트 홍보
   - Swiper 라이브러리 설정 설명
   - Spring Controller/Service/Mapper 예시 코드
   - Thymeleaf 템플릿 사용 예시

3. **Course Section (강의 목록)**
   - 역할: 강의 검색, 필터링, 표시
   - 구성 요소 (필터, 정렬, 그리드, 페이지네이션)
   - 상세한 Spring + MyBatis 연동 코드 예시:
     * Controller (`MainController.java`)
     * Service (`CourseService.java`)
     * Mapper XML (`CourseMapper.xml`)
     * Thymeleaf 템플릿 렌더링

4. **Community Section (커뮤니티)**
   - 역할: 인기 아티클 표시
   - Spring 연동 예시

5. **Footer (푸터)**
   - 역할: 사이트 정보, 링크

#### JavaScript 섹션 주석
- 전체 구조 설명 (7개 파트)
- 데이터 정의에 JSDoc 스타일 주석
- 함수별 상세 설명:
  * `formatPrice()`: 가격 포맷
  * `getStarsHTML()`: 별점 렌더링
  * `renderCategories()`: 카테고리 렌더링
  * `renderCourses()`: 강의 목록 렌더링
  * `renderPagination()`: 페이지네이션
  * `setupIntersectionObserver()`: Infinite Scroll
  * `addToCart()`: 장바구니 추가
  * `handleSearch()`: 검색 처리
- Spring 연동 방법 2가지 제시:
  * Thymeleaf Inline Script
  * REST API 사용

### 3. 반복문으로 코드 개선

#### 기존 문제점
- 하드코딩된 반복 데이터
- 유지보수 어려움
- 코드 가독성 저하

#### 개선 사항
1. **강의 데이터 생성** (line 485-495)
   ```javascript
   // 8개 BASE_COURSES를 60개로 확장
   for (let i = 9; i <= 60; i++) { 
       const randomBase = BASE_COURSES[Math.floor(Math.random() * BASE_COURSES.length)];
       COURSES.push({...randomBase, id: i, ...});
   }
   ```

2. **카테고리 렌더링** (line 556-571)
   ```javascript
   // forEach로 카테고리 버튼 동적 생성
   CATEGORIES.forEach(cat => {
       const btn = document.createElement('button');
       // ... 버튼 설정
       categoryContainer.appendChild(btn);
   });
   ```

3. **강의 카드 렌더링** (line 609-656)
   ```javascript
   // forEach로 강의 카드 동적 생성
   visibleData.forEach(course => {
       // 태그 HTML (map 사용)
       const tagsHTML = course.tags.map(tag => {...}).join('');
       
       // 학습 목표 HTML (map 사용)
       const goalsHTML = course.goals.slice(0, 3).map(goal => {...}).join('');
       
       // 카드 HTML 생성 및 삽입
       courseGrid.insertAdjacentHTML('beforeend', cardHTML);
   });
   ```

4. **페이지네이션 버튼** (line 735-749)
   ```javascript
   // for 반복문으로 페이지 번호 버튼 생성
   for (let i = 1; i <= totalPages; i++) {
       const pageBtn = document.createElement('button');
       // ... 버튼 설정
       paginationContainer.appendChild(pageBtn);
   }
   ```

### 4. Spring + MyBatis 연동 예시 코드

총 **20개의 연동 관련 주석** 추가:

#### Controller 예시
```java
@Controller
public class MainController {
    @Autowired
    private CourseService courseService;
    
    @GetMapping("/")
    public String mainPage(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String search,
        Model model
    ) {
        List<CourseVO> courses = courseService.selectCourseList(category, search);
        model.addAttribute("courses", courses);
        return "common/main/index";
    }
}
```

#### Service 예시
```java
@Service
public class CourseService {
    @Autowired
    private CourseMapper courseMapper;
    
    public List<CourseVO> selectCourseList(String category, String search, int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("search", search);
        return courseMapper.selectCourseList(params);
    }
}
```

#### MyBatis Mapper XML 예시 (실제 테이블 사용)
```xml
<select id="selectCourseList" resultType="CourseVO">
    SELECT 
        l.LECT_ID as lectId,
        l.NAME as name,
        i.NAME as instructorName,
        l.PRICE as price,
        l.THUMBNAIL as thumbnail,
        l.SHORTINT as shortInt,
        l.INTRO as intro,
        c.NAME as categoryName,
        l.USERCOUNT as userCount,
        COALESCE(AVG(r.SCORE), 0) as avgRating,
        COALESCE(COUNT(r.REVIEW_ID), 0) as reviewCount
    FROM LECTURE l
    INNER JOIN INSTRUCTOR i ON l.INST_ID = i.INST_ID
    INNER JOIN CATEGORY c ON l.CAT_ID = c.CAT_ID
    LEFT JOIN REVIEW r ON l.LECT_ID = r.LECT_ID
    <where>
        l.AVAILABILITY = 1 AND l.APPROVAL = 1
        <if test="category != null and category != 'all'">
            AND l.CAT_ID = #{category}
        </if>
        <if test="search != null">
            AND (l.NAME LIKE '%'||#{search}||'%' 
                 OR i.NAME LIKE '%'||#{search}||'%')
        </if>
    </where>
    GROUP BY l.LECT_ID, l.NAME, i.NAME, l.PRICE, l.THUMBNAIL, 
             l.SHORTINT, l.INTRO, c.NAME, l.USERCOUNT, l.REGDATE
    ORDER BY l.REGDATE DESC
</select>
```

#### Thymeleaf 템플릿 예시
```html
<!-- 카테고리 렌더링 -->
<div th:each="cat : ${categories}">
    <button th:text="${cat.name}"></button>
</div>

<!-- 강의 목록 렌더링 -->
<div th:each="course : ${courses}">
    <h3 th:text="${course.title}">강의 제목</h3>
    <p th:text="${course.instructor}">강사명</p>
</div>
```

### 5. 개선 효과

#### 코드 가독성
- ✅ 명확한 섹션 구분
- ✅ 각 기능의 역할 이해 용이
- ✅ 유지보수성 향상

#### 유지보수성
- ✅ 반복 데이터를 한 곳에서 관리
- ✅ 함수별 역할 명확화
- ✅ 주석을 통한 코드 이해도 향상

#### Spring 연동 준비
- ✅ 20개의 상세한 연동 예시
- ✅ Controller, Service, Mapper 패턴 제시
- ✅ Thymeleaf 문법 예시 제공
- ✅ REST API 대안 제시

#### 성능
- ✅ 반복문 사용으로 코드 양 감소
- ✅ 동적 렌더링으로 유연성 향상

### 6. 향후 작업 가이드

#### Spring Boot 연동 시 작업 순서
1. **VO/DTO 클래스 생성**
   - `CourseVO.java`
   - `CategoryVO.java`
   - `BannerVO.java`

2. **Mapper 인터페이스 및 XML 작성**
   - `CourseMapper.java`
   - `CourseMapper.xml`

3. **Service 클래스 작성**
   - `CourseService.java`

4. **Controller 작성**
   - `MainController.java`

5. **Thymeleaf 템플릿 수정**
   - 현재 HTML의 JavaScript 데이터를 제거
   - `th:each`, `th:if`, `th:text` 등으로 대체

#### 데이터베이스 테이블 설계 (실제 스키마 - intlearn_05_njw.sql 참조)

> 📖 상세한 스키마는 `DATABASE_SCHEMA.md` 문서를 참조하세요.

```sql
-- 카테고리 테이블 (실제: CATEGORY)
CREATE TABLE CATEGORY (
    CAT_ID VARCHAR2(30) PRIMARY KEY,        -- 카테고리 ID: CAT1, CAT2, ...
    NAME VARCHAR2(50) NOT NULL               -- 카테고리명: 데이터베이스, 웹 프로그래밍, 인공지능 등
);
-- 샘플 데이터: 10개 카테고리 (CAT1~CAT10)
-- CAT1: 데이터베이스, CAT2: 웹 프로그래밍, CAT3: 인공지능, CAT4: 모바일 앱
-- CAT5: 게임 개발, CAT6: 보안, CAT7: 데이터 분석, CAT8: 클라우드
-- CAT9: 디자인, CAT10: 교양

-- 강사 테이블 (실제: INSTRUCTOR)
CREATE TABLE INSTRUCTOR (
    INST_ID VARCHAR2(30) PRIMARY KEY,        -- 강사 ID: inst1, inst2, ...
    PASSWORD VARCHAR2(100) NOT NULL,         -- 비밀번호 (BCrypt 암호화)
    BIRTH DATE NOT NULL,                     -- 생년월일
    EMAIL VARCHAR2(100) NOT NULL,            -- 이메일
    NAME VARCHAR2(100) NOT NULL,             -- 강사명
    PHONE VARCHAR2(11) NOT NULL,             -- 전화번호
    PROFILE VARCHAR2(30) NOT NULL,           -- 프로필 이미지
    ACTIVATION NUMBER(1) NOT NULL,           -- 활성화 상태 (0:비활성, 1:활성)
    APPROVAL NUMBER(1) NOT NULL,             -- 승인 상태 (0:대기, 1:승인)
    REGDATE DATE DEFAULT SYSDATE NOT NULL,   -- 가입일
    REGIP VARCHAR2(30) NOT NULL              -- 가입 IP
);
-- 샘플 데이터: 10명 강사 (inst1~inst10)

-- 강의 테이블 (실제: LECTURE)
CREATE TABLE LECTURE (
    LECT_ID VARCHAR2(30) PRIMARY KEY,        -- 강의 ID: L1, L2, ...
    NAME VARCHAR2(100) NOT NULL,             -- 강의명
    PRICE NUMBER(6) NOT NULL,                -- 가격 (예: 30000, 50000)
    SHORTINT VARCHAR2(100) NOT NULL,         -- 짧은 소개
    INTRO CLOB NOT NULL,                     -- 상세 소개
    COUNT NUMBER(2) NOT NULL,                -- 챕터 수
    LENGTH NUMBER(3) NOT NULL,               -- 총 강의 시간(분)
    AVAILABILITY NUMBER(1) NOT NULL,         -- 공개 여부 (0:비공개, 1:공개)
    CAT_ID VARCHAR2(30),                     -- 카테고리 ID (FK)
    INST_ID VARCHAR2(30),                    -- 강사 ID (FK)
    THUMBNAIL VARCHAR2(30) NOT NULL,         -- 썸네일 이미지
    USERCOUNT NUMBER(5) NOT NULL,            -- 수강생 수
    REGDATE DATE DEFAULT SYSDATE NOT NULL,   -- 등록일
    REGIP VARCHAR2(30) NOT NULL,             -- 등록 IP
    APPROVAL NUMBER(1) NOT NULL,             -- 승인 상태 (0:거절, 1:승인)
    REJECT_REASON VARCHAR2(1000),            -- 거절 사유
    FOREIGN KEY (CAT_ID) REFERENCES CATEGORY(CAT_ID),
    FOREIGN KEY (INST_ID) REFERENCES INSTRUCTOR(INST_ID)
);
-- 샘플 데이터: 10개 강의 (L1~L10)
-- L1: SQL 입문 (30,000원), L2: Java 마스터 (50,000원)
-- L3: AI 첫걸음 (40,000원), L4: 앱 개발 (55,000원)
-- L5: Unity 게임 (60,000원) - 승인 거절, L6: 정보보안 (45,000원)
-- L7: 데이터 분석 (50,000원), L8: AWS 클라우드 (80,000원)
-- L9: UI 디자인 (35,000원) - 비공개, L10: 교양 철학 (20,000원)

-- 리뷰 테이블 (실제: REVIEW)
CREATE TABLE REVIEW (
    REVIEW_ID VARCHAR2(30) PRIMARY KEY,      -- 리뷰 ID
    SCORE NUMBER(1) NOT NULL,                -- 평점 (1~5)
    CONTENT VARCHAR2(1000) NOT NULL,         -- 리뷰 내용
    REGDATE DATE DEFAULT SYSDATE NOT NULL,   -- 작성일
    REGIP VARCHAR2(30) NOT NULL,             -- 작성 IP
    LECT_ID VARCHAR2(30),                    -- 강의 ID (FK)
    USER_ID VARCHAR2(30),                    -- 사용자 ID (FK)
    FOREIGN KEY (LECT_ID) REFERENCES LECTURE(LECT_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
-- 리뷰 평점을 집계하여 강의별 평균 평점 및 리뷰 수를 계산
```

#### 실제 테이블과 컬럼 매핑

| HTML 필드 | 실제 테이블 | 실제 컬럼 | 비고 |
|----------|-----------|----------|------|
| category.id | CATEGORY | CAT_ID | CAT1, CAT2, ... |
| category.name | CATEGORY | NAME | 데이터베이스, 웹 프로그래밍, ... |
| course.id | LECTURE | LECT_ID | L1, L2, ... |
| course.title | LECTURE | NAME | 강의명 |
| course.instructor | INSTRUCTOR | NAME | 강사명 (조인 필요) |
| course.price | LECTURE | PRICE | 가격 (원) |
| course.rating | REVIEW | AVG(SCORE) | 평균 평점 (집계) |
| course.reviewCount | REVIEW | COUNT(*) | 리뷰 수 (집계) |
| course.category | LECTURE | CAT_ID | 카테고리 ID |
| course.description | LECTURE | INTRO | 상세 소개 |
| course.thumbnail | LECTURE | THUMBNAIL | 썸네일 이미지 |

## 참고사항
- 원본 파일 (`3차 메인페이지 테스트 백업.html`)은 그대로 유지
- 새 파일 (`index.html`)은 독립적으로 사용 가능
- Thymeleaf 네임스페이스 추가됨: `xmlns:th="http://www.thymeleaf.org"`
- CDN 링크는 그대로 유지 (Tailwind CSS, Lucide Icons, Swiper)

## 검증 완료
- ✅ HTML 구조 유효성 검증 완료
- ✅ Maven 빌드 성공 확인
- ✅ 1,308 라인, 58KB 크기
- ✅ 모든 주요 섹션 포함 확인
- ✅ Spring 연동 주석 20개 포함
