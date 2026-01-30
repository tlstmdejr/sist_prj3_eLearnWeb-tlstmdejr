package kr.co.sist.user.member.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.sist.user.member.UserDTO;
import kr.co.sist.user.member.UserDomain;

// user 모듈의 로그인 컨트롤러 - 빈 이름을 명시적으로 지정하여 충돌 방지
@RequestMapping("/user/member/login")
@Controller("userLoginController")
public class LoginController {
	
	@Autowired
	private LoginService ls;
	
	@GetMapping("/loginFrm")
	public String stuLogin() {
		
		return "user/member/login/loginFrm";
	}
	@PostMapping("/loginProcess")
	public String stuLoginProcess(UserDTO sDTO, Model model, HttpSession session) {
		
		UserDomain sd = ls.loginUser(sDTO);
		
		if(sd != null) { // 로그인 성공
			// 세션에 필요한 정보 저장
			session.setAttribute("userId", sd.getId());
			session.setAttribute("userName", sd.getName());
			
			return "redirect:/"; // 메인 페이지로 이동
		} else { // 로그인 실패
			model.addAttribute("msg", "아이디 또는 비밀번호를 확인해주세요.");
			return "user/member/login/loginFrm";
		}
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
