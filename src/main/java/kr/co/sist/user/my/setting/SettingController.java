package kr.co.sist.user.my.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private SettingService ss;

    /**
     * 내 정보 설정 페이지 (조회)
     * - 프로필 사진 & 닉네임 & 아이디 & 자기소개 조회
     * - 이메일, 비밀번호(마스킹), 휴대폰 번호 조회
     * @param session 세션에서 userId 가져옴
     * @param model 뷰에 전달할 데이터
     * @return 설정 페이지 뷰
     */
    @GetMapping("")
    public String mySetting(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        
        // TODO: 사용자 정보 조회
        // SettingDomain sd = ss.getSettingInfo(userId);
        // model.addAttribute("userInfo", sd);
        
        return "user/my/setting/index";
    }

    // ===========================
    // 프로필 정보 수정
    // ===========================

    /**
     * 프로필 사진 변경 처리
     * @param img 업로드할 프로필 이미지
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updateImg")
    @ResponseBody
    public String updateImg(MultipartFile img, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        
        // TODO: 프로필 이미지 변경
        // 1. 파일 업로드 처리
        // 2. ss.modifyImg(userId, imgPath) 호출
        
        return "success";
    }

    /**
     * 닉네임 변경 처리
     * @param name 새 닉네임
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updateNick")
    @ResponseBody
    public String updateNick(String name, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        
        // TODO: 닉네임 변경
        // int result = ss.modifyNick(userId, name);
        
        return "success";
    }

    /**
     * 자기소개 변경 처리
     * @param intro 새 자기소개
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updateIntro")
    @ResponseBody
    public String updateIntro(String intro, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        
        // TODO: 자기소개 변경
        // int result = ss.modifyIntro(userId, intro);
        
        return "success";
    }

    // ===========================
    // 이메일, 비밀번호, 휴대폰 수정
    // ===========================

    /**
     * 이메일 변경 처리
     * @param email 새 이메일
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updateEmail")
    @ResponseBody
    public String updateEmail(String email, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        
        // TODO: 이메일 변경
        // 1. 이메일 인증 필요시 인증 로직 추가
        // 2. ss.modifyEmail(userId, email) 호출
        
        return "success";
    }

    /**
     * 비밀번호 변경 처리
     * @param currentPass 현재 비밀번호
     * @param newPass 새 비밀번호
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updatePass")
    @ResponseBody
    public String updatePass(String currentPass, String newPass, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        
        // TODO: 비밀번호 변경
        // 1. 현재 비밀번호 확인 (BCrypt)
        // 2. 새 비밀번호 암호화 후 저장
        // int result = ss.modifyPass(userId, currentPass, newPass);
        
        return "success";
    }

    /**
     * 휴대폰 번호 변경 처리
     * @param phone 새 휴대폰 번호
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updatePhone")
    @ResponseBody
    public String updatePhone(String phone, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        
        // TODO: 휴대폰 번호 변경
        // 1. 휴대폰 인증 필요시 인증 로직 추가
        // 2. ss.modifyPhone(userId, phone) 호출
        
        return "success";
    }
}
// class
