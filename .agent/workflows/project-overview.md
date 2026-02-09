---
description: 프로젝트 전체 기능 및 워크플로우 정리
---

# sist_prj3_eLearnWeb-tlstmdejr 프로젝트 기능 정리

> 이 문서는 프로젝트의 모든 기능을 **공통**, **관리자**, **강사**, **유저** 4가지 역할로 분류하여 정리한 워크플로우입니다.

---

## 1. 프로젝트 구조 개요

```
kr.co.sist
├── common/          # 공통 기능 (아이디/비밀번호 찾기, 이메일 서비스)
├── admin/           # 관리자 기능 (회원관리, 강사승인)
├── instructor/      # 강사 기능 (회원가입, 로그인, 대시보드)
├── user/            # 유저 기능 (회원가입, 로그인, 프로필, 설정)
└── config/          # 설정 (WebMvcConfig)
```

---

## 2. 공통 기능 (Common)

### 2.1 이메일 서비스
- **파일**: `common/email/EmailService.java`
- **기능**: 이메일 발송 (인증번호 전송)

### 2.2 아이디/비밀번호 찾기
- **컨트롤러**: `CommonMemberController.java`
- **URL 매핑**: `/common/member`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 아이디 찾기 폼 | GET | `/findIdFrm` | 아이디 찾기 페이지 표시 |
| 비밀번호 찾기 폼 | GET | `/findPwFrm` | 비밀번호 찾기 페이지 표시 |
| 아이디 찾기 처리 | POST | `/findIdProcess` | 전화번호로 아이디 조회 (AJAX) |
| SMS 인증번호 발송 | POST | `/sendAuthCode` | 인증번호 SMS 발송 (AJAX) |
| SMS 인증번호 확인 | POST | `/verifyAuthCode` | 인증번호 검증 (AJAX) |
| 비밀번호 재설정 | POST | `/resetPassword` | 새 비밀번호로 변경 (AJAX) |

### 워크플로우: 아이디 찾기
```
1. 사용자 → /common/member/findIdFrm 접속
2. 전화번호 입력 → SMS 인증번호 발송 (/sendAuthCode)
3. 인증번호 확인 (/verifyAuthCode)
4. 성공 시 아이디 표시 (/findIdProcess)
```

### 워크플로우: 비밀번호 찾기
```
1. 사용자 → /common/member/findPwFrm 접속
2. 아이디 + 전화번호 입력 → SMS 인증
3. 인증 성공 → 새 비밀번호 입력
4. 비밀번호 변경 (/resetPassword)
```

---

## 3. 관리자 기능 (Admin)

### 3.1 로그인/로그아웃
- **컨트롤러**: `AdminLoginController.java`
- **URL 매핑**: `/admin/login`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 로그인 폼 | GET | `/loginFrm` | 관리자 로그인 페이지 |
| 로그인 처리 | POST | `/loginProcess` | 로그인 검증 후 세션 생성 |
| 로그아웃 | GET | `/logout` | 세션 무효화 |

### 3.2 회원 관리
- **컨트롤러**: `AdminMemberController.java`
- **URL 매핑**: `/admin/member`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 회원 목록 | GET | `/userList` | 전체 사용자 목록 조회 |
| 강사 목록 | GET | `/instructorList` | 전체 강사 목록 조회 |
| 강사 승인 | POST | `/approveInstructor` | 강사 승인 처리 |

### 워크플로우: 관리자 로그인 및 회원 관리
```
1. 관리자 → /admin/login/loginFrm 접속
2. 아이디/비밀번호 입력 → 로그인 처리
3. 성공 시 → /admin/member/userList (회원 목록)
4. 강사 목록 확인 → 승인 대기 강사 승인 처리
5. 로그아웃 → /admin/login/logout
```

---

## 4. 강사 기능 (Instructor)

### 4.1 회원가입
- **컨트롤러**: `InstructorController.java`
- **URL 매핑**: `/instructor/member`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 회원가입 폼 | GET | `/joinFrm` | 강사 회원가입 페이지 |
| 회원가입 처리 | POST | `/joinProcess` | 선서 확인 후 회원가입 |
| 아이디 중복확인 | GET | `/overlapId` | 아이디 중복 체크 (AJAX) |

### 4.2 로그인/로그아웃
- **컨트롤러**: `InstructorLoginController.java`
- **URL 매핑**: `/instructor/login`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 로그인 폼 | GET | `/loginFrm` | 강사 로그인 페이지 |
| 로그인 처리 | POST | `/loginProcess` | 로그인 (승인 상태 확인) |
| 로그아웃 | GET | `/logout` | 세션 무효화 |

### 4.3 대시보드
- **컨트롤러**: `InstructorDashboardController.java`
- **URL 매핑**: `/instructor`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 대시보드 | GET | `/dashboard` | 강사 메인 대시보드 |

### 워크플로우: 강사 회원가입 및 로그인
```
1. 강사 → /instructor/member/joinFrm 접속
2. 정보 입력 + 선서 작성 → 회원가입 처리
3. 관리자 승인 대기 (approval = 0)
4. 관리자가 승인 처리 (approval = 1)
5. 강사 → /instructor/login/loginFrm 로그인
6. 성공 시 → /instructor/dashboard
```

---

## 5. 유저 기능 (User)

### 5.1 메인 페이지
- **컨트롤러**: `MainController.java`
- **URL 매핑**: `/`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 메인 페이지 | GET | `/` | 메인 페이지 (index.html) |

### 5.2 회원가입
- **컨트롤러**: `UserController.java`
- **URL 매핑**: `/user/member`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 회원가입 폼 | GET | `/joinFrm` | 회원가입 페이지 |
| 회원가입 처리 | POST | `/joinProcess` | 유효성 검사 후 가입 |
| 아이디 중복확인 | GET | `/overlapId` | 아이디 중복 체크 (AJAX) |
| 닉네임 중복확인 | GET | `/overlapName` | 닉네임 중복 체크 (AJAX) |
| 전화번호 중복확인 | GET | `/overlapPhone` | 전화번호 중복 체크 (AJAX) |

### 5.3 로그인/로그아웃
- **컨트롤러**: `LoginController.java`
- **URL 매핑**: `/user/member/login`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 로그인 폼 | GET | `/loginFrm` | 로그인 페이지 |
| 로그인 처리 | POST | `/loginProcess` | BCrypt 비밀번호 검증 |
| 로그아웃 | GET/POST | `/logout` | 세션 무효화 |

### 5.4 프로필 조회
- **컨트롤러**: `ProfileController.java`
- **URL 매핑**: `/user/my/profile`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 프로필 페이지 | GET | `` (빈 경로) | 내 프로필 조회 |

### 5.5 설정 (Setting)
- **컨트롤러**: `SettingController.java`
- **URL 매핑**: `/user/my/setting`

| 기능 | HTTP Method | URL | 설명 |
|------|-------------|-----|------|
| 설정 페이지 | GET | `` (빈 경로) | 내 정보 설정 페이지 |
| 프로필 수정 | POST | `/updateProfile` | 이미지/닉네임/자기소개 수정 |
| 이메일 변경 | POST | `/updateEmail` | 이메일 변경 (AJAX) |
| 비밀번호 변경 | POST | `/updatePass` | 비밀번호 변경 (AJAX) |
| 전화번호 변경 | POST | `/updatePhone` | 전화번호 변경 (AJAX) |
| SMS 인증 발송 | POST | `/sendSms` | 전화번호 인증 SMS 발송 |
| SMS 인증 확인 | POST | `/verifySms` | SMS 인증번호 검증 |
| 이메일 인증 발송 | POST | `/sendEmailAuth` | 이메일 인증번호 발송 |
| 이메일 인증 확인 | POST | `/verifyEmailAuth` | 이메일 인증번호 검증 |
| 회원 탈퇴 | POST | `/deleteAccount` | 계정 비활성화 처리 |

### 워크플로우: 유저 회원가입
```
1. 사용자 → /user/member/joinFrm 접속
2. 아이디/닉네임/전화번호 중복확인 (AJAX)
3. 정보 입력 → 회원가입 처리
4. 성공 → 로그인 페이지로 이동
```

### 워크플로우: 유저 로그인 및 설정
```
1. 사용자 → /user/member/login/loginFrm 접속
2. 아이디/비밀번호 입력 → 로그인
3. 성공 시 → 메인 페이지 (/)
4. /user/my/setting → 내 정보 설정
5. 프로필 사진, 닉네임, 자기소개 수정
6. 이메일/비밀번호/전화번호 변경 (인증 필요)
7. 회원 탈퇴 시 → 계정 비활성화
```

---

## 6. 데이터베이스 테이블 매핑

| 역할 | 테이블 | 주요 컬럼 |
|------|--------|----------|
| 관리자 | MANAGER | ADM_ID, PASSWORD |
| 강사 | INSTRUCTOR | INST_ID, PASSWORD, APPROVAL, ACTIVATION |
| 유저 | USERS | USER_ID, PASSWORD, ACTIVATION |
| 발송 이력 | TRANSMISSION_HISTORY | TRANS_ID, PHONE, EMAIL |

---

## 7. 주요 서비스 및 보안 기능

### 7.1 암호화
- **비밀번호**: BCrypt (단방향 해시)
- **이메일/이름**: AES 양방향 암호화 (고정 IV)
- **설정 파일**: `application-dev.properties`
  - `user.crypto.key=test1234`
  - `user.crypto.salt=ABCDEF1234567890`

### 7.2 SMS 서비스
- **제공자**: Solapi API
- **용도**: 인증번호 발송

### 7.3 이메일 서비스
- **SMTP**: Gmail (smtp.gmail.com:587)
- **용도**: 이메일 인증번호 발송

### 7.4 세션 관리
- **타임아웃**: 30분 (`server.servlet.session.timeout=30m`)
- **저장 정보**: userId, userName, userEmail (유저) / adminId (관리자) / instructorId (강사)

---

## 8. 템플릿 구조

```
templates/
├── index.html                    # 메인 페이지
├── fragments/                    # 공통 레이아웃 조각
├── admin/
│   └── member/
│       ├── login/loginFrm.html   # 관리자 로그인
│       ├── userList.html         # 회원 목록
│       └── instructorList.html   # 강사 목록
├── instructor/
│   ├── member/
│   │   ├── joinFrm.html          # 강사 회원가입
│   │   └── login/loginFrm.html   # 강사 로그인
│   └── dashboard/index.html      # 강사 대시보드
├── user/
│   ├── member/
│   │   ├── joinFrm.html          # 유저 회원가입
│   │   └── login/loginFrm.html   # 유저 로그인
│   └── my/
│       ├── profile/profile.html  # 프로필
│       └── setting/settingPage.html # 설정
└── common/
    └── member/
        ├── findIdFrm.html        # 아이디 찾기
        └── findPwFrm.html        # 비밀번호 찾기
```

---

## 9. Mapper 파일 구조

```
mappers/
├── admin/
│   ├── adminMemberMapper.xml     # 회원/강사 목록 조회
│   └── loginMapper.xml           # 관리자 로그인
├── instructor/
│   ├── instructorMemberMapper.xml # 강사 등록
│   └── loginMapper.xml           # 강사 로그인
├── user/
│   ├── memberMapper.xml          # 유저 등록
│   ├── loginMapper.xml           # 유저 로그인
│   ├── profileMapper.xml         # 프로필 조회
│   └── settingMapper.xml         # 설정 정보 조회/수정
└── common/
    └── commonMemberMapper.xml    # 아이디/비번 찾기
```
