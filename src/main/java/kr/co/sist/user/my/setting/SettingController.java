package kr.co.sist.user.my.setting;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

/**
 * 내 정보 설정 Controller
 * - 프로필 사진 & 닉네임 & 아이디 & 자기소개 조회/수정
 * - 이메일 조회 및 변경
 * - 비밀번호 조회 및 변경
 * - 휴대폰 번호 조회 및 변경
 */
@Controller
@RequestMapping("/user/my/setting")
public class SettingController {

    @org.springframework.beans.factory.annotation.Value("${user.upload-dir}")
    private String uploadDir;

    @Autowired
    private SettingService ss;

    /**
     * 내 정보 설정 페이지 (조회)
     * - 프로필 사진 & 닉네임 & 아이디 & 자기소개 조회
     * - 이메일, 비밀번호(마스킹), 휴대폰 번호 조회
     */
    @GetMapping("")
    public String mySetting(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        SettingDomain sd = ss.getSettingInfo(userId);
        model.addAttribute("user", sd);
        return "user/my/setting/settingPage";
    }

    /**
     * 이메일 인증 팝업 페이지
     */
    @GetMapping("/emailAuth")
    public String emailAuth() {
        return "user/my/setting/emailAuth";
    }

    // ===========================
    // 프로필 정보 수정
    // ===========================

    /**
     * 프로필 정보 통합 수정 (이미지, 닉네임, 자기소개)
     */
    @PostMapping("/updateProfile")
    @ResponseBody
    public String updateProfile(
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "intro", required = false) String intro,
            HttpSession session) {

        String userId = (String) session.getAttribute("userId");
        String resultMsg = "success";

        // ... (updateProfile method)

        // 1. 프로필 이미지 변경
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // 외부 경로 저장 (C:/upload/profile/{userId}/)
                // 1. 사용자 ID별 폴더 생성
                File saveDir = new File(uploadDir + "profile" + File.separator + userId);
                if (!saveDir.exists())
                    saveDir.mkdirs();

                // 2. 원본 파일명 사용 (충돌 방지를 위해 UUID 등 고려 가능하지만, 요구사항에 따라 원본 유지)
                String originalFileName = profileImage.getOriginalFilename();
                // String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
                String saveFileName = originalFileName;

                // 3. 파일 저장
                File saveFile = new File(saveDir.getAbsolutePath(), saveFileName);
                profileImage.transferTo(saveFile);

                // 4. DB에는 웹 접근 경로 저장 (/images/profile/{userId}/파일명)
                String webPath = "/images/profile/" + userId + "/" + saveFileName;
                ss.modifyImg(userId, webPath);
            } catch (IOException e) {
                e.printStackTrace();
                return "fail_img";
            }
        }

        // 2. 닉네임 변경
        if (name != null) {
            ss.modifyNick(userId, name);
        }

        // 3. 자기소개 변경
        if (intro != null) {
            ss.modifyIntro(userId, intro);
        }

        return resultMsg;
    }

    // ===========================
    // 이메일, 비밀번호, 휴대폰 수정
    // ===========================

    /**
     * 이메일 변경 처리
     * 
     * @param email   새 이메일
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX) - success / fail / same_email
     */
    @PostMapping("/updateEmail")
    @ResponseBody
    public String updateEmail(String email, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        int result = ss.modifyEmail(userId, email);

        if (result == 1) {
            return "success";
        } else if (result == -1) {
            return "same_email";
        } else {
            return "fail";
        }
    }

    /**
     * 비밀번호 변경 처리
     * 
     * @param currentPass 현재 비밀번호
     * @param newPass     새 비밀번호
     * @param session     세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updatePass")
    @ResponseBody
    public String updatePass(String currentPass, String newPass, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        int result = ss.modifyPass(userId, currentPass, newPass);

        if (result == -1) {
            return "pw_mismatch";
        }

        return result == 1 ? "success" : "fail";
    }

    /**
     * 휴대폰 번호 변경 처리
     * 
     * @param phone   새 휴대폰 번호
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updatePhone")
    @ResponseBody
    public String updatePhone(String phone, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        int result = ss.modifyPhone(userId, phone);
        return result == 1 ? "success" : "fail";
    }

    // ===========================
    // SMS 인증 관련
    // ===========================

    @Autowired
    private kr.co.sist.common.member.CommonMemberService cms;

    @Autowired
    private kr.co.sist.common.email.EmailService emailService;

    /**
     * 인증번호 발송
     */
    @GetMapping("/sendSms")
    @ResponseBody
    public String sendSms(String phone, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        SettingDomain sd = ss.getSettingInfo(userId);
        if (sd != null && sd.getPhone() != null && sd.getPhone().equals(phone)) {
            return "same_phone";
        }

        String code = cms.sendPhoneVerificationCode(phone);
        if (code != null) {
            session.setAttribute("smsCode", code);
            return "success";
        }
        return "fail";
    }

    /**
     * 인증번호 확인
     */
    @PostMapping("/verifySms")
    @ResponseBody
    public String verifySms(String code, HttpSession session) {
        String key = (String) session.getAttribute("smsCode");
        if (key != null && key.equals(code)) {
            session.removeAttribute("smsCode"); // 인증 성공 시 세션에서 제거
            return "success";
        }
        return "fail";
    }

    // ===========================
    // 이메일 인증 관련
    // ===========================

    /**
     * 이메일 인증번호 발송
     */
    @GetMapping("/sendEmailAuth")
    @ResponseBody
    public String sendEmailAuth(String email, HttpSession session) {
        System.out.println("[Debug] sendEmailAuth 호출됨: " + email);
        String code = emailService.sendAuthCode(email);
        System.out.println("[Debug] 인증코드 생성결과: " + code);

        if (code != null) {
            session.setAttribute("emailCode", code);
            System.out.println("[Debug] sendEmailAuth 반환: email_sent");
            return "email_sent";
        }
        System.out.println("[Debug] sendEmailAuth 반환: fail");
        return "fail";
    }

    /**
     * 이메일 인증번호 확인
     */
    @PostMapping("/verifyEmailAuth")
    @ResponseBody
    public String verifyEmailAuth(String code, HttpSession session) {
        String key = (String) session.getAttribute("emailCode");
        if (key != null && key.equals(code)) {
            session.removeAttribute("emailCode");
            return "verified";
        }
        return "fail";
    }

    // ===========================
    // 회원 탈퇴
    // ===========================
    @PostMapping("/deleteAccount")
    @ResponseBody
    public String deleteAccount(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        int result = ss.withdrawalUser(userId);
        if (result == 1) {
            session.invalidate(); // 세션 만료
            return "success";
        }
        return "fail";
    }

}
