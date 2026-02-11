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

#### MyBatis Mapper XML 예시
```xml
<select id="selectCourseList" resultType="CourseVO">
    SELECT course_id, title, instructor, price, rating
    FROM courses
    <where>
        <if test="category != null">
            AND category = #{category}
        </if>
        <if test="search != null">
            AND title LIKE '%'||#{search}||'%'
        </if>
    </where>
    ORDER BY created_at DESC
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

#### 데이터베이스 테이블 설계
```sql
-- 카테고리 테이블
CREATE TABLE categories (
    category_id VARCHAR2(20) PRIMARY KEY,
    category_name VARCHAR2(50) NOT NULL,
    icon_name VARCHAR2(50),
    is_active CHAR(1) DEFAULT 'Y'
);

-- 강의 테이블
CREATE TABLE courses (
    course_id NUMBER PRIMARY KEY,
    title VARCHAR2(200) NOT NULL,
    instructor_name VARCHAR2(50),
    price NUMBER,
    original_price NUMBER,
    rating NUMBER(2,1),
    review_count NUMBER,
    category_id VARCHAR2(20),
    level VARCHAR2(20),
    description VARCHAR2(1000),
    created_at DATE DEFAULT SYSDATE,
    is_active CHAR(1) DEFAULT 'Y',
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- 배너 테이블
CREATE TABLE banners (
    banner_id NUMBER PRIMARY KEY,
    badge VARCHAR2(20),
    title VARCHAR2(200),
    description VARCHAR2(500),
    button_text VARCHAR2(50),
    bg_color VARCHAR2(50),
    display_order NUMBER,
    is_active CHAR(1) DEFAULT 'Y',
    created_at DATE DEFAULT SYSDATE
);
```

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
