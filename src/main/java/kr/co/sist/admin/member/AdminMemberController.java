package kr.co.sist.admin.member;

import java.util.List;
import kr.co.sist.user.member.UserDomain;
import kr.co.sist.instructor.member.InstructorDomain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    // ============================
    // 회원 관리
    // ============================

    @GetMapping("/userList")
    public String userList(Model model) {
        List<UserDomain> list = adminMemberService.getUserList();
        model.addAttribute("userList", list);
        return "admin/member/userList";
    }

    /**
     * 회원 상세 조회 (AJAX)
     */
    @GetMapping("/userDetail")
    @ResponseBody
    public UserDomain userDetail(String id) {
        return adminMemberService.getUser(id);
    }

    // ============================
    // 강사 관리
    // ============================

    @GetMapping("/instructorList")
    public String instructorList(Model model) {
        List<InstructorDomain> list = adminMemberService.getInstructorList();
        model.addAttribute("instructorList", list);
        return "admin/member/instructorList";
    }

    /**
     * 강사 상세 조회 (AJAX)
     */
    @GetMapping("/instructorDetail")
    @ResponseBody
    public InstructorDomain instructorDetail(String instId) {
        return adminMemberService.getInstructor(instId);
    }

    /**
     * 강사 승인 처리 (폼 제출)
     */
    @PostMapping("/approveInstructor")
    public String approveInstructor(String instId) {
        adminMemberService.approveInstructor(instId);
        return "redirect:/admin/member/instructorList";
    }

    /**
     * 강사 승인 처리 (AJAX)
     */
    @PostMapping("/approveInstructorAjax")
    @ResponseBody
    public String approveInstructorAjax(String instId) {
        boolean success = adminMemberService.approveInstructor(instId);
        return success ? "success" : "fail";
    }

}
