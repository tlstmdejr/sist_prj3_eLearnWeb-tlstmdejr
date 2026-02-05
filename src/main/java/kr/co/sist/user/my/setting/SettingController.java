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

    @Autowired
    private SettingService ss;

    /**
     * 내 정보 설정 페이지 (조회)
     * - 프로필 사진 & 닉네임 & 아이디 & 자기소개 조회
     * - 이메일, 비밀번호(마스킹), 휴대폰 번호 조회
     * 
     * @param session 세션에서 userId 가져옴
     * @param model   뷰에 전달할 데이터
     * @return 설정 페이지 뷰
     */
    @GetMapping("")
    public String mySetting(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");

        // 사용자 정보 조회
        SettingDomain sd = ss.getSettingInfo(userId);
        model.addAttribute("user", sd);

        return "user/my/setting/settingPage";
    }

    // ===========================
    // 프로필 정보 수정
    // ===========================

    /**
     * 프로필 사진 변경 처리
     * 
     * @param img     업로드할 프로필 이미지
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updateImg")
    @ResponseBody
    public String updateImg(@RequestParam("profileImage") MultipartFile profileImage, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        String resultMsg = "fail";

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // 저장 경로 설정
                File saveDir = new File("src/main/resources/static/images/profile");
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }

                String originalFileName = profileImage.getOriginalFilename();
                String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
                String saveFileName = userId + "_profile" + ext;

                File saveFile = new File(saveDir.getAbsolutePath(), saveFileName);
                profileImage.transferTo(saveFile);

                // DB 업데이트
                String webPath = "/images/profile/" + saveFileName;
                int result = ss.modifyImg(userId, webPath);

                if (result == 1) {
                    resultMsg = "success";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultMsg;
    }

    /**
     * 닉네임 변경 처리
     * 
     * @param name    새 닉네임
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updateNick")
    @ResponseBody
    public String updateNick(String name, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        int result = ss.modifyNick(userId, name);
        return result == 1 ? "success" : "fail";
    }

    /**
     * 자기소개 변경 처리
     * 
     * @param intro   새 자기소개
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updateIntro")
    @ResponseBody
    public String updateIntro(String intro, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        int result = ss.modifyIntro(userId, intro);
        return result == 1 ? "success" : "fail";
    }

    // ===========================
    // 이메일, 비밀번호, 휴대폰 수정
    // ===========================

    /**
     * 이메일 변경 처리
     * 
     * @param email   새 이메일
     * @param session 세션에서 userId 가져옴
     * @return 결과 메시지 (AJAX)
     */
    @PostMapping("/updateEmail")
    @ResponseBody
    public String updateEmail(String email, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        int result = ss.modifyEmail(userId, email);
        return result == 1 ? "success" : "fail";
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

}
// class
