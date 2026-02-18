package kr.co.sist.common.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private CommonMainService cms;

    @GetMapping("/")
    public String main(HttpSession session, HttpServletRequest request, Model model) {
        List<CommonMainDomain> categories = cms.getCategoryList();
        List<CommonMainDomain> courses = cms.getCourseList();

        model.addAttribute("categories", categories);
        model.addAttribute("courses", courses);

        return "index";
    }
}
