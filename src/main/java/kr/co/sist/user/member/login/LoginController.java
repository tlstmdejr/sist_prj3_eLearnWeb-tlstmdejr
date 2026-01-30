package kr.co.sist.user.member.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.sist.user.member.StudentDTO;

@RequestMapping("/user/member/login")
@Controller
public class LoginController {
	
	@Autowired
	private LoginService ls;
	
	@GetMapping("/loginFrm")
	public String stuLogin() {
		
		return "/loginFrm";
	}
	@PostMapping("/loginProcess")
	public String stuLoginProcess(StudentDTO sDTO,Model model,HttpSession session,HttpServletRequest request) {
		
		return "/loginFrm";
	}
	
}
//class



// @PostMapping("/loginProcess")
// 	public String loginProcess(LoginDTO lDTO,HttpServletRequest request,Model model,HttpSession session) {
		
// 		String url="/login/loginProcess";
		
// 		lDTO.setIp(request.getRemoteAddr());
// 		MemberDomain md= ls.searchOneMember(lDTO);
		
// 		 System.out.println(lDTO); System.out.println(md);
		 
// 		if("s".equals(lDTO.getResult())) {
// 			session.setAttribute("userId", lDTO.getId());
// 			session.setAttribute("userName", md.getName());
// 			session.setAttribute("userEmail", md.getEmail());
// 			url="redirect:/";//성공하면 메인
// 		}
// 		model.addAttribute("errFlag",lDTO.getResult()!=null);//null or F error flag
// 		model.addAttribute("errMsg",lDTO.getResult()==null?"아이디가존재하지않습니다.":"비밀번호가 일치하지 않습니다.");//null or F error flag
// 		return url;
// 	}
// 	//로그아웃 세션정보 제거 , 세션무력화
// 	@RequestMapping(value="/logout",method= {GET,POST})
// 	public String logout(HttpSession session) {
// 		session.removeAttribute("userId");
// 		session.removeAttribute("userName");
// 		session.removeAttribute("userEmail");
		
// 		session.invalidate();
// 		return "redirect:/";
// 	}
