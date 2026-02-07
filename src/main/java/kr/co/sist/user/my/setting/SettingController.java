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
     */
    @GetMapping("")
    public String mySetting(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        SettingDomain sd = ss.getSettingInfo(userId);
        model.addAttribute("user", sd);
        return "user/my/setting/settingPage";
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

        // 1. 프로필 이미지 변경
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // F드라이브 경로에 맞게 저장 경로 확인 필요 (상대경로 사용 시 프로젝트 루트 기준)
                File saveDir = new File("src/main/resources/static/images/profile");
                if (!saveDir.exists())
                    saveDir.mkdirs();

                String originalFileName = profileImage.getOriginalFilename();
                String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
                String saveFileName = userId + "_profile" + ext;

                File saveFile = new File(saveDir.getAbsolutePath(), saveFileName);
                profileImage.transferTo(saveFile);

                String webPath = "/images/profile/" + saveFileName;
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
