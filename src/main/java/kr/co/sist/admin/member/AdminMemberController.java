package kr.co.sist.admin.member;

import java.util.List;
import kr.co.sist.user.member.UserDomain;
import kr.co.sist.instructor.member.InstructorDomain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int PAGINATION_BLOCK_SIZE = 10;

    private final AdminMemberService adminMemberService;

    public AdminMemberController(AdminMemberService adminMemberService) {
        this.adminMemberService = adminMemberService;
    }

    // ============================
    // 회원 관리
    // ============================

    @GetMapping("/userList")
    public String userList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        int currentPage = normalizePage(page);
        int pageSize = normalizePageSize(size);

        int totalCount = adminMemberService.getUserTotalCount();
        int totalPages = calculateTotalPages(totalCount, pageSize);
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        List<UserDomain> list = adminMemberService.getUserList(currentPage, pageSize);

        int startPage = ((currentPage - 1) / PAGINATION_BLOCK_SIZE) * PAGINATION_BLOCK_SIZE + 1;
        int endPage = Math.min(startPage + PAGINATION_BLOCK_SIZE - 1, totalPages);

        model.addAttribute("userList", list);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

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
    public String instructorList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        int currentPage = normalizePage(page);
        int pageSize = normalizePageSize(size);

        int totalCount = adminMemberService.getInstructorTotalCount();
        int totalPages = calculateTotalPages(totalCount, pageSize);
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        List<InstructorDomain> list = adminMemberService.getInstructorList(currentPage, pageSize);

        int startPage = ((currentPage - 1) / PAGINATION_BLOCK_SIZE) * PAGINATION_BLOCK_SIZE + 1;
        int endPage = Math.min(startPage + PAGINATION_BLOCK_SIZE - 1, totalPages);

        model.addAttribute("instructorList", list);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

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
     * 강사 승인 (폼 방식)
     */
    @PostMapping("/approveInstructor")
    public String approveInstructor(String instId) {
        adminMemberService.approveInstructor(instId);
        return "redirect:/admin/member/instructorList";
    }

    /**
     * 강사 승인 (AJAX)
     */
    @PostMapping("/approveInstructorAjax")
    @ResponseBody
    public String approveInstructorAjax(String instId) {
        boolean success = adminMemberService.approveInstructor(instId);
        return success ? "success" : "fail";
    }

    private int normalizePage(int page) {
        return page <= 0 ? 1 : page;
    }

    private int normalizePageSize(int size) {
        if (size < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        if (size > MAX_PAGE_SIZE) {
            return MAX_PAGE_SIZE;
        }
        return size;
    }

    private int calculateTotalPages(int totalCount, int pageSize) {
        if (totalCount <= 0) {
            return 1;
        }
        return (int) Math.ceil((double) totalCount / pageSize);
    }
}
