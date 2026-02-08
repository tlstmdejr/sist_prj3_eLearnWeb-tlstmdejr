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
        // 선서 확인
        if (!"나는 강사로서의 의무를 다하겠습니다".equals(iDTO.getOath())) {
            model.addAttribute("msg", "선서 내용이 올바르지 않습니다.");
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

}
