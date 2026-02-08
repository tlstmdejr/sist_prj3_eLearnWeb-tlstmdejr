package kr.co.sist.common.member;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 공통 - 회원 멤버 컨트롤러 (아이디/비번 찾기)
 */
@Controller
@RequestMapping("/common/member")
public class CommonMemberController {

    private final CommonMemberService commonMemberService;

    public CommonMemberController(CommonMemberService commonMemberService) {
        this.commonMemberService = commonMemberService;
    }

    @GetMapping("/findIdFrm")
    public String findIdFrm() {
        return "common/member/findIdFrm";
    }

    @GetMapping("/findPwFrm")
    public String findPwFrm() {
        return "common/member/findPwFrm";
    }

    @PostMapping("/findIdProcess")
    @ResponseBody
    public String findIdProcess(String phone) {
        return commonMemberService.findId(phone);
    }

    @PostMapping("/sendAuthCode")
    @ResponseBody
    public String sendAuthCode(String phone, HttpSession session) {
        String code = commonMemberService.sendAuthCode(phone);
        if (code != null) {
            session.setAttribute("authCode", code);
            // session.setMaxInactiveInterval(180); // 3분 유효시간 설정 등 고려
            return "success";
        }
        return "fail";
    }

    @PostMapping("/verifyAuthCode")
    @ResponseBody
    public boolean verifyAuthCode(String code, HttpSession session) {
        String sessionCode = (String) session.getAttribute("authCode");
        return sessionCode != null && sessionCode.equals(code);
    }

    @PostMapping("/resetPassword")
    @ResponseBody
    public boolean resetPassword(String id, String password) {
        return commonMemberService.updatePassword(id, password);
    }

}
