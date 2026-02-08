package kr.co.sist.admin.member.login;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.admin.member.AdminDTO;
import kr.co.sist.admin.member.AdminDomain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 관리자 - 로그인 컨트롤러
 */
@Controller
@RequestMapping("/admin/login")
public class AdminLoginController {

    @GetMapping("/loginFrm")
    public String loginFrm() {
        return "admin/member/login/loginFrm";
    }

    private final AdminLoginService adminLoginService;

    public AdminLoginController(AdminLoginService adminLoginService) {
        this.adminLoginService = adminLoginService;
    }

    @PostMapping("/loginProcess")
    public String loginProcess(AdminDTO adminDTO, HttpSession session, Model model) {
        AdminDomain adminDomain = adminLoginService.login(adminDTO);
        if (adminDomain != null) {
            session.setAttribute("adminId", adminDomain.getId());
            return "redirect:/admin/member/userList"; // 로그인 성공 시 사용자 목록으로 이동
        }
        model.addAttribute("msg", "아이디 또는 비밀번호를 확인해주세요.");
        return "admin/member/login/loginFrm";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login/loginFrm";
    }

}
