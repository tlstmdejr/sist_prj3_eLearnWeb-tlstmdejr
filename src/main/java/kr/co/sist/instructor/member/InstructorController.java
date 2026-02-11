package kr.co.sist.instructor.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public String joinProcess(InstructorDTO iDTO, Model model) {
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
