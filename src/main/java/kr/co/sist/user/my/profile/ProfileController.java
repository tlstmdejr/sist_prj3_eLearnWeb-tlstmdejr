package kr.co.sist.user.my.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.user.member.UserDomain;

/**
 * 내 프로필 조회 Controller
 * - 프로필 사진 & 닉네임 & 아이디 조회
 * - 자기소개 조회
 */
@Controller
@RequestMapping("/user/my/profile")
public class ProfileController {

    @Autowired
    private ProfileService ps;

    /**
     * 내 프로필 페이지
     * - 프로필 사진 & 닉네임 & 아이디 조회
     * - 자기소개 조회
     * 
     * @param session 세션에서 userId 가져옴
     * @param model   뷰에 전달할 데이터
     * @return 프로필 조회 뷰
     */
    @GetMapping("/mypageMain")
    public String myProfile(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");

        // 프로필 정보 조회
        UserDomain ud = ps.selectOneProfile(userId);
        model.addAttribute("profile", ud);

        return "user/my/profile/mypageMain";
    }
}
// class
