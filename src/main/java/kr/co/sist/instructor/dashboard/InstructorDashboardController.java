package kr.co.sist.instructor.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 강사 대시보드 컨트롤러
 */
@Controller
@RequestMapping("/instructor")
public class InstructorDashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "instructor/dashboard/index";
    }

}
