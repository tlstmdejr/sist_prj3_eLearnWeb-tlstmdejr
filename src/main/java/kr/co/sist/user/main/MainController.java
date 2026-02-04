package kr.co.sist.user.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(HttpSession session, HttpServletRequest request) {
        System.out.println("---- MainController 진입 ----");
        System.out.println("세션 ID: " + session.getId());
        System.out.println("세션 userId: " + session.getAttribute("userId"));

        return "index";
    }
}
