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

---

<!-- ================================================================
     아래 섹션은 Claude Opus 4.6 (2026-02-13)이 코드 리뷰 후 추가한 내용입니다.
     기존 에이전트가 작성한 위의 내용은 수정하지 않았습니다.
     ================================================================ -->

# 코드 리뷰 및 개선 제안 (by Claude Opus 4.6)
> 작성일: 2026-02-13
> 대상 파일: `src/main/resources/templates/common/main/index.html`

---

## 1. 보안 취약점 (Critical)

### 1-1. XSS (Cross-Site Scripting) 취약점
**위치**: index.html 라인 1058~1113 (강의 카드 렌더링)

**문제**: 강의 데이터(`title`, `instructor`, `description`, `goals`, `tags`)가 템플릿 리터럴을 통해 **이스케이프 없이** HTML에 직접 삽입됩니다.

```javascript
// 현재 (위험)
h3 class="...">${course.title}</h3>
<p class="...">${course.description}</p>
<span>...</span>${tag}</span>
```

현재는 하드코딩 더미 데이터여서 문제가 없지만, Spring 연동 후 DB에서 데이터를 가져오면 악의적 사용자가 아래와 같은 데이터를 입력할 수 있습니다:

```
제목: <script>document.cookie를 해커서버로 전송</script>
```

**해결 방안**:
```javascript
// 방법 1: 이스케이프 유틸리티 함수 추가
const escapeHTML = (str) => {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
};

// 사용
`<h3>${escapeHTML(course.title)}</h3>`

// 방법 2: textContent 사용 (DOM 조작 방식)
const titleEl = document.createElement('h3');
titleEl.textContent = course.title;  // 자동 이스케이프
```

### 1-2. addToCart 함수 - 장바구니 ID 미전달
**위치**: index.html 라인 1119~1123, 1221~1232

**문제**: 장바구니 버튼 클릭 시 `courseId`를 서버에 보내지 않습니다.

```javascript
// 현재 (문제)
btn.addEventListener('click', (e) => {
    e.stopPropagation();
    addToCart();  // 어떤 강의인지 알 수 없음
});
```

**해결 방안**:
```javascript
btn.addEventListener('click', (e) => {
    e.stopPropagation();
    const courseId = e.currentTarget.dataset.id;
    addToCart(courseId);
});

const addToCart = (courseId) => {
    state.cartCount++;
    cartBadge.textContent = state.cartCount;
    // Spring 연동 시:
    // fetch('/api/cart/add', { method: 'POST', body: JSON.stringify({ courseId }) })
};
```

---

## 2. 성능 문제 (High)

### 2-1. lucide.createIcons() 과다 호출
**위치**: 라인 958, 1140, 1194, 1257, 1305

**문제**: 카테고리 클릭, 페이지 변경, 검색할 때마다 **전체 페이지의 아이콘을 재탐색**합니다. 60개 강의 카드에 각각 여러 아이콘이 있으므로 DOM 탐색이 매번 반복됩니다.

**해결 방안**:
```javascript
// 방법 1: 새로 추가된 영역에서만 아이콘 생성
const refreshIcons = (container) => {
    const icons = container.querySelectorAll('[data-lucide]');
    icons.forEach(icon => lucide.createElement(icon));
};

// renderCourses 마지막에
refreshIcons(courseGrid);  // 전체 대신 courseGrid만 탐색

// 방법 2: SVG를 직접 사용 (아이콘 라이브러리 호출 제거)
// 자주 쓰는 아이콘(star, check, shopping-cart 등)은
// SVG 문자열로 미리 캐싱
const ICON_CACHE = {};
const getIcon = (name) => {
    if (!ICON_CACHE[name]) {
        const temp = document.createElement('div');
        temp.innerHTML = `<i data-lucide="${name}"></i>`;
        lucide.createIcons({ nodes: [temp.firstChild] });
        ICON_CACHE[name] = temp.innerHTML;
    }
    return ICON_CACHE[name];
};
```

### 2-2. 이벤트 리스너 누적 (메모리 누수)
**위치**: 라인 1118~1123

**문제**: `renderCourses()`가 호출될 때마다 `.add-to-cart-btn`에 새 이벤트 리스너를 등록합니다. `courseGrid.innerHTML = ''`로 DOM은 제거되지만, 매 렌더링마다 `querySelectorAll`로 **전체 문서**를 탐색하는 것은 비효율적입니다.

**해결 방안 - 이벤트 위임(Event Delegation)**:
```javascript
// 초기화 시 한 번만 등록
courseGrid.addEventListener('click', (e) => {
    const btn = e.target.closest('.add-to-cart-btn');
    if (btn) {
        e.stopPropagation();
        addToCart(btn.dataset.id);
    }
});

// renderCourses 안의 이벤트 리스너 등록 코드 제거
```

### 2-3. 검색 입력 디바운싱 미적용
**위치**: 라인 1239~1250

**문제**: 키보드 입력 시 매 글자마다 `renderCourses()`가 호출됩니다. "스프링 부트" 입력 시 최소 5~6번 필터링+렌더링이 실행됩니다.

**해결 방안**:
```javascript
let searchTimer = null;
const handleSearch = (e) => {
    state.searchQuery = e.target.value;
    searchInput.value = state.searchQuery;
    mobileSearchInput.value = state.searchQuery;

    clearTimeout(searchTimer);
    searchTimer = setTimeout(() => {
        state.currentPage = 1;
        state.page1Current = 10;
        renderCourses(true);
    }, 300);  // 300ms 후 실행
};
```

### 2-4. CDN 버전 고정 안 됨 (운영 위험)
**위치**: 라인 41

**문제**:
```html
<!-- 위험: 어느 날 갑자기 최신 버전으로 바뀌면 사이트가 깨질 수 있음 -->
<script src="https://unpkg.com/lucide@latest"></script>
```

**해결 방안**:
```html
<!-- 버전을 고정하여 안정성 확보 -->
<script src="https://unpkg.com/lucide@0.344.0"></script>
```

또한 Tailwind CSS도 CDN 버전은 개발용이므로, 프로덕션에서는 빌드 도구를 통해 사용해야 합니다.

---

## 3. 접근성 (Accessibility) 문제 (Medium)

### 3-1. ARIA 속성 누락

```html
<!-- 현재 (접근성 미흡) -->
<button id="mobile-menu-btn" class="...">
    <i data-lucide="menu"></i>
</button>

<!-- 개선안 -->
<button id="mobile-menu-btn" class="..."
        aria-label="메뉴 열기"
        aria-expanded="false"
        aria-controls="mobile-menu">
    <i data-lucide="menu"></i>
</button>
```

### 3-2. 키보드 네비게이션 미지원

강의 카드의 호버 상세 정보 카드가 `:hover`에만 반응합니다. 키보드 사용자는 상세 정보를 볼 수 없습니다.

```css
/* 개선안: focus-within 추가 */
.group:focus-within .hidden {
    display: block;
}
```

```html
<!-- 강의 카드에 tabindex와 role 추가 -->
<div class="relative group hover:z-50 h-full" tabindex="0" role="article">
```

### 3-3. Skip Navigation 누락

스크린 리더 사용자가 매번 헤더를 듣지 않도록 건너뛰기 링크가 필요합니다.

```html
<!-- body 태그 바로 뒤에 추가 -->
<a href="#course-grid" class="sr-only focus:not-sr-only focus:absolute focus:z-[100] focus:bg-white focus:p-4 focus:text-emerald-600 focus:font-bold">
    본문 바로가기
</a>
```

---

## 4. UX 개선 제안 (Medium)

### 4-1. 호버 카드 화면 밖 잘림 문제
**위치**: 라인 1080

**문제**: 화면 오른쪽 끝이나 하단 끝에 있는 카드의 호버 상세 정보가 화면 밖으로 잘릴 수 있습니다.

**해결 방안 (JavaScript로 위치 동적 계산)**:
```javascript
// 호버 시 상세 카드 위치를 뷰포트 기준으로 조정
courseGrid.addEventListener('mouseenter', (e) => {
    const card = e.target.closest('.group');
    if (!card) return;
    const hoverCard = card.querySelector('.group-hover\\:block');
    if (!hoverCard) return;

    const rect = card.getBoundingClientRect();
    // 오른쪽 넘침 체크
    if (rect.right + 20 > window.innerWidth) {
        hoverCard.style.left = 'auto';
        hoverCard.style.right = '-10px';
    }
    // 하단 넘침 체크
    if (rect.bottom + 200 > window.innerHeight) {
        hoverCard.style.top = 'auto';
        hoverCard.style.bottom = '-10px';
    }
}, true);
```

### 4-2. 모바일 메뉴 외부 클릭 시 닫기 미구현
**위치**: 라인 1253~1258

```javascript
// 개선안: 외부 클릭으로 메뉴 닫기
document.addEventListener('click', (e) => {
    if (!mobileMenu.contains(e.target) && !mobileMenuBtn.contains(e.target)) {
        mobileMenu.classList.add('hidden');
        mobileMenuBtn.innerHTML = '<i data-lucide="menu" class="w-6 h-6"></i>';
        lucide.createIcons();
    }
});
```

### 4-3. "맨 위로 가기" 버튼 부재

긴 강의 목록을 스크롤한 후 상단으로 돌아갈 방법이 없습니다.

```html
<!-- Footer 앞에 추가 -->
<button id="scroll-top-btn"
        class="fixed bottom-6 right-6 w-12 h-12 bg-emerald-600 text-white rounded-full shadow-lg hover:bg-emerald-700 transition-all opacity-0 invisible z-50"
        aria-label="맨 위로 가기">
    <i data-lucide="chevron-up" class="w-6 h-6 mx-auto"></i>
</button>

<script>
const scrollTopBtn = document.getElementById('scroll-top-btn');
window.addEventListener('scroll', () => {
    if (window.scrollY > 500) {
        scrollTopBtn.classList.remove('opacity-0', 'invisible');
    } else {
        scrollTopBtn.classList.add('opacity-0', 'invisible');
    }
});
scrollTopBtn.addEventListener('click', () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
});
</script>
```

### 4-4. 정렬 버튼 동작 미구현
**위치**: 라인 466~469

추천순/최신순/평점순 버튼은 시각적으로만 존재하고 실제 정렬 로직이 없습니다.

```javascript
// 개선안
const SORT_FUNCTIONS = {
    recommend: (a, b) => b.reviewCount - a.reviewCount,
    latest:    (a, b) => b.id - a.id,
    rating:    (a, b) => b.rating - a.rating,
};

// state에 정렬 옵션 추가
state.sortBy = 'recommend';

// renderCourses 안에서 필터링 후 정렬 추가
filtered.sort(SORT_FUNCTIONS[state.sortBy]);
```

---

## 5. 코드 구조 및 품질 (Medium)

### 5-1. rating 타입 불일치
**위치**: 라인 863

**문제**: `toFixed(1)`은 **문자열**을 반환하지만, `getStarsHTML()`은 `Math.floor(rating)`으로 숫자 연산을 수행합니다.

```javascript
// 현재 (버그 가능성)
rating: (4 + Math.random()).toFixed(1)  // "4.7" (string)

// 개선안
rating: parseFloat((4 + Math.random()).toFixed(1))  // 4.7 (number)
```

> 참고: JavaScript에서 `Math.floor("4.7")`은 `4`를 반환하므로 현재 동작은 합니다. 하지만 타입 불일치는 향후 비교 연산 등에서 버그를 유발할 수 있습니다.

### 5-2. 더미 데이터가 매번 랜덤 변경
**위치**: 라인 856~865

**문제**: `Math.random()`을 사용하므로 페이지 새로고침 시마다 강의 목록이 달라집니다. 개발/테스트 시 재현 불가능한 상태를 만듭니다.

**해결 방안**:
```javascript
// 시드 기반 난수 생성기 (결정적 결과)
const seededRandom = (seed) => {
    const x = Math.sin(seed) * 10000;
    return x - Math.floor(x);
};

for (let i = 9; i <= 60; i++) {
    const randomBase = BASE_COURSES[Math.floor(seededRandom(i) * BASE_COURSES.length)];
    COURSES.push({
        ...randomBase,
        id: i,
        title: `${randomBase.title} - 심화과정 ${i}`,
        reviewCount: Math.floor(seededRandom(i + 100) * 1000),
        rating: parseFloat((4 + seededRandom(i + 200)).toFixed(1))
    });
}
```

### 5-3. 카테고리 표시 로직 하드코딩
**위치**: 라인 1086

```javascript
// 현재 (확장 불가)
course.category === 'dev' ? '개발 · 프로그래밍' : 'IT · 데이터'
```

카테고리가 6개인데 2가지만 분기합니다. security, mobile, game은 전부 "IT · 데이터"로 표시됩니다.

**해결 방안**:
```javascript
// CATEGORIES 배열에서 이름을 찾아서 표시
const getCategoryName = (categoryId) => {
    const cat = CATEGORIES.find(c => c.id === categoryId);
    return cat ? cat.name : categoryId;
};

// 사용
`<span>${getCategoryName(course.category)}</span>`
```

---

## 6. SEO 개선 (Low)

### 6-1. 메타 태그 부족
```html
<!-- head 태그 안에 추가 권장 -->
<meta name="description" content="인런(InLearn) - 개발, 보안, 데이터 사이언스 등 IT 분야 온라인 강의 플랫폼">
<meta name="keywords" content="온라인강의,이러닝,프로그래밍,개발강의,인런">

<!-- Open Graph (소셜 미디어 공유용) -->
<meta property="og:title" content="인런 (InLearn) - 성장을 위한 온라인 강의 플랫폼">
<meta property="og:description" content="최고의 강사진과 함께하는 실전 중심의 커리큘럼">
<meta property="og:type" content="website">
<meta property="og:url" content="https://inlearn.co.kr">
```

### 6-2. href="#" 남용
**위치**: 라인 132, 201~204, 552, 602, 611~613, 619~621 등

모든 링크가 `href="#"`로 되어 있습니다. 검색엔진은 이를 "빈 링크"로 판단합니다. Spring 연동 전이라도 의미 있는 경로를 넣어두는 것이 좋습니다.

```html
<!-- 개선안: Thymeleaf 경로 미리 설정 -->
<a th:href="@{/}" href="/index.html">InLearn</a>
<a th:href="@{/community}" href="/community.html">커뮤니티</a>
```

---

## 7. 획기적 개선안 - 전체 아키텍처

### 7-1. JavaScript 모듈화 (ES Modules)

현재 1,300줄이 넘는 단일 HTML 파일에 모든 로직이 들어있습니다. 유지보수와 팀 협업에 불리합니다.

**제안 구조**:
```
src/main/resources/
├── static/
│   ├── js/
│   │   ├── main.js          (초기화 & 이벤트 바인딩)
│   │   ├── data.js           (카테고리, 더미 데이터)
│   │   ├── render.js         (렌더링 함수들)
│   │   ├── utils.js          (formatPrice, escapeHTML 등)
│   │   └── state.js          (상태 관리)
│   └── css/
│       └── custom.css        (커스텀 스타일)
└── templates/
    └── common/main/
        └── index.html        (HTML 구조만)
```

이렇게 분리하면:
- 각 파일이 200줄 이내로 유지
- Git 충돌 감소 (팀원별 다른 파일 작업 가능)
- 테스트 용이
- 브라우저 캐싱 활용 가능

### 7-2. Thymeleaf Fragment 활용

하나의 거대한 HTML 대신 Fragment로 분리하면 재사용과 유지보수가 쉬워집니다.

```html
<!-- templates/fragments/header.html -->
<header th:fragment="header" class="sticky top-0 ...">
    <!-- 헤더 내용 -->
</header>

<!-- templates/fragments/footer.html -->
<footer th:fragment="footer" class="bg-white ...">
    <!-- 푸터 내용 -->
</footer>

<!-- index.html -->
<div th:replace="~{fragments/header :: header}"></div>
<main>
    <!-- 메인 콘텐츠만 -->
</main>
<div th:replace="~{fragments/footer :: footer}"></div>
```

### 7-3. REST API + Fetch로 전환 (SPA 스타일)

현재 주석에 Thymeleaf 방식(SSR)과 REST API 방식(CSR) 두 가지가 혼재되어 있습니다. 프로젝트 규모를 고려하면 **하이브리드 방식**을 추천합니다.

```
초기 로딩: Thymeleaf (SSR) - SEO 유리, 빠른 첫 화면
이후 상호작용: REST API (CSR) - 카테고리 전환, 검색, 페이징
```

```java
// REST Controller 추가
@RestController
@RequestMapping("/api")
public class CourseApiController {
    @GetMapping("/courses")
    public ResponseEntity<Page<CourseVO>> getCourses(
        @RequestParam(defaultValue = "all") String category,
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "recommend") String sort
    ) {
        return ResponseEntity.ok(courseService.getCourses(category, search, page, size, sort));
    }
}
```

---

## 8. 우선순위 정리

| 순위 | 항목 | 심각도 | 작업량 |
|------|------|--------|--------|
| 1 | XSS 이스케이프 함수 추가 | Critical | 30분 |
| 2 | addToCart에 courseId 전달 | Critical | 10분 |
| 3 | 검색 디바운싱 | High | 10분 |
| 4 | 이벤트 위임 적용 | High | 20분 |
| 5 | CDN 버전 고정 | High | 5분 |
| 6 | rating 타입 수정 | Medium | 5분 |
| 7 | 카테고리명 하드코딩 수정 | Medium | 10분 |
| 8 | 정렬 기능 구현 | Medium | 30분 |
| 9 | 접근성(ARIA, 키보드) | Medium | 1시간 |
| 10 | JS 모듈화 | Low | 2~3시간 |
| 11 | Thymeleaf Fragment 분리 | Low | 1~2시간 |
| 12 | SEO 메타 태그 | Low | 15분 |

---

## 9. 즉시 적용 가능한 빠른 수정 코드

아래 변경 사항들은 기존 동작을 해치지 않으면서 바로 적용 가능합니다.

### head 태그에 메타 태그 추가
```html
<meta name="description" content="인런(InLearn) - IT 분야 전문 온라인 강의 플랫폼. 개발, 보안, 데이터 사이언스 강의.">
```

### escapeHTML 유틸리티 (라인 911 근처에 추가)
```javascript
const escapeHTML = (str) => {
    if (typeof str !== 'string') return str;
    const map = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' };
    return str.replace(/[&<>"']/g, (c) => map[c]);
};
```

### 디바운스 유틸리티 (라인 911 근처에 추가)
```javascript
const debounce = (fn, delay = 300) => {
    let timer;
    return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => fn(...args), delay);
    };
};

// 사용 (라인 1249~1250 교체)
searchInput.addEventListener('input', debounce(handleSearch));
mobileSearchInput.addEventListener('input', debounce(handleSearch));
```
