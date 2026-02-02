package kr.co.sist.user.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/member")
public class UserController {

    @Autowired
    private UserService us;

    // 학생 회원가입
    @GetMapping("/joinFrm")
    public String joinUser() {

        return "/user/member/joinFrm";
    }

    @PostMapping("/joinProcess")
    public String joinUserProcess(UserDTO sDTO, Model model, HttpServletRequest request) {

        sDTO.setRegip(request.getRemoteAddr());
        boolean flag = us.addUser(sDTO);
        model.addAttribute("memberResult", flag);
        return "/user/member/joinProcess";
    }

    // 중복 확인
    // 중복 확인
    @GetMapping("/overlapId")
    @ResponseBody
    public String chkId(String id) {
        return "";
    }

    @GetMapping("/overlapNick")
    @ResponseBody
    public String chkNick(String name) {
        return "";
    }

    // 학생 내 정보 조회
    @GetMapping("/searchInfo")
    public String searchStuInfo(HttpSession session, Model model) {
        return "";
    }// setting

    // 학생 내 정보 수정
    @PostMapping("/updateInfo")
    public String updateStuInfo(Model model) {
        return "";
    }// setting

    @PostMapping("/updateInfoProcess")
    public String updateStuInfoProcess(MultipartFile mf, UserDTO sDTO, HttpSession session) {
        return "";
    }// setting

    // 이메일, 전화번호처럼 한 가지씩만 데이터가 변경되는 메소드는 모두 별개로 있어야 한다.
    public String updateStuEmailProcess() {
        return "";
    }// setting

    public String updateStuPhoneProcess() {
        return "";
    }// setting

    // 학생 회원 탈퇴
    @PostMapping("/withdrawalProcess")
    public String stuWithdrawalProcess(UserDTO sDTO, HttpSession session) {
        return "";
    }
    // 세션에서 받아온 id를 DTO에 넣어 서비스로 전달

}
