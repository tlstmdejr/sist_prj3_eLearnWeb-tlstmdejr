package kr.co.sist.admin.member;

import java.util.List;
import kr.co.sist.user.member.UserDomain;
import kr.co.sist.instructor.member.InstructorDomain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 관리자 - 회원 관리 컨트롤러
 */
@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    public AdminMemberController(AdminMemberService adminMemberService) {
        this.adminMemberService = adminMemberService;
    }

    @GetMapping("/userList")
    public String userList(Model model) {
        List<UserDomain> list = adminMemberService.getUserList();
        model.addAttribute("userList", list);
        return "admin/member/userList";
    }

    @GetMapping("/instructorList")
    public String instructorList(Model model) {
        List<InstructorDomain> list = adminMemberService.getInstructorList();
        model.addAttribute("instructorList", list);
        return "admin/member/instructorList";
    }

    @PostMapping("/approveInstructor")
    public String approveInstructor(String instId) {
        adminMemberService.approveInstructor(instId);
        return "redirect:/admin/member/instructorList";
    }

}
