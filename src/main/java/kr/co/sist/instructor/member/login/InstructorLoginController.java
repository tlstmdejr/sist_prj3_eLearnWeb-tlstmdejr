package kr.co.sist.instructor.member.login;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import kr.co.sist.instructor.member.InstructorDTO;
import kr.co.sist.instructor.member.InstructorDomain;

/**
 * 강사 - 로그인 컨트롤러
 */
@Controller
@RequestMapping("/instructor/login")
public class InstructorLoginController {

    @GetMapping("/loginFrm")
    public String loginFrm() {
        return "instructor/member/login/loginFrm";
    }

    private final InstructorLoginService instructorLoginService;

    public InstructorLoginController(InstructorLoginService instructorLoginService) {
        this.instructorLoginService = instructorLoginService;
    }

    @PostMapping("/loginProcess")
    public String loginProcess(InstructorDTO iDTO, HttpSession session, Model model) {
        InstructorDomain iDomain = instructorLoginService.login(iDTO);
        if (iDomain != null) {
            session.setAttribute("instructorId", iDomain.getInstId());
            return "redirect:/instructor/dashboard"; // 이동할 페이지 확인 필요
        }
        model.addAttribute("msg", "아이디/비밀번호를 확인하거나 승인 대기 중입니다.");
        return "common/member/loginFrm";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/common/member/loginFrm";
    }

}
