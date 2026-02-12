package kr.co.sist.instructor.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.instructor.member.InstructorDTO;
import kr.co.sist.instructor.member.InstructorService;

/**
 * 강사 - 회원가입(Member) 컨트롤러
 */
@Controller
@RequestMapping("/instructor/member")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/joinFrm")
    public String joinFrm() {
        return "instructor/member/joinFrm";
    }

    @PostMapping("/joinProcess")
    public String joinProcess(InstructorDTO iDTO, Model model, HttpServletRequest request) {
        // 클라이언트 IP 설정
        iDTO.setRegip(request.getRemoteAddr());

        // profile이 비어있으면 기본값 설정 (DB NOT NULL 제약조건 대응)
        if (iDTO.getProfile() == null || iDTO.getProfile().trim().isEmpty()) {
            iDTO.setProfile(" ");
        }

        // 서버 측 선서 검증 (프론트엔드 우회 방지)
        if (iDTO.getOath() == null || !iDTO.getOath().trim().equals("나는 강사로서의 의무를 다하겠습니다")) {
            model.addAttribute("errorMsg", "선서 문구를 정확히 입력해주세요.");
            return "instructor/member/joinFrm";
        }

        if (instructorService.addInstructor(iDTO)) {
            return "instructor/member/joinSuccess";
        }
        return "instructor/member/joinFrm";
    }

    @GetMapping("/overlapId")
    @ResponseBody
    public String overlapId(String id) {
        return instructorService.chkId(id);
    }

    /**
     * 이름 중복 확인 (AJAX)
     */
    @GetMapping("/overlapName")
    @ResponseBody
    public String overlapName(String name) {
        return instructorService.chkName(name);
    }

    /**
     * 전화번호 중복 확인 (AJAX)
     */
    @GetMapping("/overlapPhone")
    @ResponseBody
    public String overlapPhone(String phone) {
        return instructorService.chkPhone(phone);
    }

}
