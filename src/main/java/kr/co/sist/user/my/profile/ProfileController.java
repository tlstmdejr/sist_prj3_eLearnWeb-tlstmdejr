package kr.co.sist.user.my.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.user.my.setting.SettingDomain;
import kr.co.sist.user.my.setting.SettingService;

@Controller
@RequestMapping("/user/my/profile")
public class ProfileController {

    @Autowired
    private SettingService ss;

    @GetMapping("")
    public String myProfile(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");

        // SettingService를 재사용하여 프로필 정보 조회
        SettingDomain profile = ss.getSettingInfo(userId);
        model.addAttribute("profile", profile);

        return "user/my/profile/profile";
    }
}
