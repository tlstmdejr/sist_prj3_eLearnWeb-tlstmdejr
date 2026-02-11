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

    /**
     * 공통 로그인 페이지 (사용자/강사 탭 전환)
     */
    @GetMapping("/loginFrm")
    public String loginFrm() {
        return "common/member/loginFrm";
    }

    /**
     * 공통 회원가입 선택 페이지 (학생/강사 선택)
     */
    @GetMapping("/joinSelectFrm")
    public String joinSelectFrm() {
        return "common/member/joinSelectFrm";
    }

    @GetMapping("/findIdFrm")
    public String findIdFrm() {
        return "common/member/findIdFrm";
    }

    @GetMapping("/findPwFrm")
    public String findPwFrm() {
        return "common/member/findPwFrm";
    }

    @PostMapping("/sendIdAuthCode")
    @ResponseBody
    public String sendIdAuthCode(String phone, HttpSession session) {
        String code = commonMemberService.sendIdAuthCode(phone);

        if ("not_found".equals(code)) {
            return "not_found";
        }

        if (code != null) {
            session.setAttribute("findIdCode", code);
            session.setAttribute("findIdPhone", phone); // 인증 성공 시 아이디 조회용
            session.setMaxInactiveInterval(300); // 5분
            return "success";
        }
        return "fail";
    }

    @PostMapping("/verifyIdAuthCode")
    @ResponseBody
    public String verifyIdAuthCode(String code, HttpSession session) {
        String sessionCode = (String) session.getAttribute("findIdCode");
        String phone = (String) session.getAttribute("findIdPhone");

        if (sessionCode != null && sessionCode.equals(code) && phone != null) {
            session.removeAttribute("findIdCode");
            session.removeAttribute("findIdPhone");
            // 인증 성공 시 아이디 반환
            return commonMemberService.findIdByPhone(phone);
        }
        return "fail";
    }

    @PostMapping("/sendPwAuthCode")
    @ResponseBody
    public String sendPwAuthCode(String type, String id, String name, HttpSession session) {
        String result = commonMemberService.sendPwAuthCode(type, id, name);

        if ("not_found".equals(result)) {
            return "not_found";
        }

        if (result != null && result.contains("##")) {
            // 응답 형식: 마스킹이메일##인증코드
            String[] parts = result.split("##");
            String maskedEmail = parts[0];
            String code = parts[1];
            session.setAttribute("findPwCode", code);
            session.setMaxInactiveInterval(300); // 5분
            return maskedEmail; // 마스킹된 이메일 반환
        }
        return "fail";
    }

    @PostMapping("/verifyPwAuthCode")
    @ResponseBody
    public String verifyPwAuthCode(String code, HttpSession session) {
        String sessionCode = (String) session.getAttribute("findPwCode");

        if (sessionCode != null && sessionCode.equals(code)) {
            session.removeAttribute("findPwCode");
            return "success";
        }
        return "fail";
    }

    @PostMapping("/resetPassword")
    @ResponseBody
    public boolean resetPassword(String type, String id, String password) {
        return commonMemberService.updatePassword(type, id, password);
    }

}
