package kr.co.sist.user.member.login;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String stuLoginProcess(UserDTO uDTO, Model model, HttpSession session) {
		System.out.println("---- 로그인 프로세스 진입 ----");
		UserDomain ud = ls.loginUser(uDTO);
		System.out.println("로그인 결과 ud: " + ud);

		if (ud != null) { // 로그인 성공
			// 세션에 필요한 정보 저장
			session.setAttribute("userId", ud.getId());
			session.setAttribute("userName", ud.getName());
			session.setAttribute("userEmail", ud.getEmail());

			System.out.println("세션 설정 완료: " + session.getId());
			System.out.println("세션 설정 완료: " + session.getAttribute("userName"));
			System.out.println("세션 설정 완료: " + session.getAttribute("userEmail"));
			System.out.println("SET userId: " + ud.getId());

			return "redirect:/"; // 메인 페이지로 이동
		} else { // 로그인 실패
			System.out.println("로그인 실패: ud is null");
			model.addAttribute("msg", "아이디 또는 비밀번호를 확인해주세요.");
			return "common/member/loginFrm";
		}
	}

	@RequestMapping(value = "/logout", method = { GET, POST })
	public String logout(HttpSession session) {
		System.out.println("---- 로그아웃 요청 진입 ----");
		System.out.println("세션 ID: " + session.getId());
		System.out.println("삭제 전 userId: " + session.getAttribute("userId"));

		session.removeAttribute("userId");
		session.removeAttribute("userName");
		session.removeAttribute("userEmail");

		session.invalidate();

		System.out.println("---- 세션 무효화 완료 ----");
		return "redirect:/";
	}

}
// class

// @PostMapping("/loginProcess")
// public String loginProcess(LoginDTO lDTO,HttpServletRequest request,Model
// model,HttpSession session) {

// String url="/login/loginProcess";

// lDTO.setIp(request.getRemoteAddr());
// MemberDomain md= ls.searchOneMember(lDTO);

// System.out.println(lDTO); System.out.println(md);

// if("s".equals(lDTO.getResult())) {
// session.setAttribute("userId", lDTO.getId());
// session.setAttribute("userName", md.getName());
// session.setAttribute("userEmail", md.getEmail());
// url="redirect:/";//성공하면 메인
// }
// model.addAttribute("errFlag",lDTO.getResult()!=null);//null or F error flag
// model.addAttribute("errMsg",lDTO.getResult()==null?"아이디가존재하지않습니다.":"비밀번호가
// 일치하지 않습니다.");//null or F error flag
// return url;
// }
// //로그아웃 세션정보 제거 , 세션무력화
// @RequestMapping(value="/logout",method= {GET,POST})
// public String logout(HttpSession session) {
// session.removeAttribute("userId");
// session.removeAttribute("userName");
// session.removeAttribute("userEmail");

// session.invalidate();
// return "redirect:/";
// }
// 로그인 로직(
// Controller
// 와
// Service
// 단계)은 이미 핵심을 잘 잡으셨습니다. (아이디/비번 검증, 암호화, 세션 처리)

// 하지만 "웹 애플리케이션 전반의 로그인 프로세스" 관점에서는 다음 3가지를 추가로 고려하면 완성도가 훨씬 높아집니다.

// 1. 인터셉터 (Interceptor) - "보안 문지기"
// 지금 만드신 건 **"로그인을 시켜주는 기능"**뿐입니다. 더 중요한 건 **"로그인 안 한 사람이 못 들어오게 막는 기능"**입니다.

// 상황: 로그인 안 한 사용자가 주소창에 http://localhost:8080/user/my/info를 직접 입력한다면?
// 해결: 컨트롤러마다 if(session.getAttribute("userId") == null)을 쓰는 건 비효율적입니다.
// Interceptor를 설정해서 로그인 필요한 페이지 접근 시 자동으로 로그인 창으로 튕겨내야 합니다.
// 2. 아이디 저장 (Cookie)
// 상황: 사용자가 매번 아이디를 치기 귀찮아합니다.
// 해결: 로그인 화면의 [v] 아이디 저장 체크박스 기능입니다. 로그인 성공 시 **쿠키(Cookie)**에 아이디를 암호화 없이
// 저장해두고, 다음 접속 시
// Controller
// 에서 쿠키를 확인해 아이디 입력창을 미리 채워줍니다.
// 3. 접속 이력 갱신 (DB Update)
// 상황: 관리자가 "이 회원 마지막으로 언제 접속했지?"라고 알고 싶거나, 해킹 의심 시 접속 IP를 확인해야 합니다.
// 해결: 로그인 성공 직후(Session 만들기 전)에 DB에 UPDATE 쿼리를 날립니다.
// 예: UPDATE USERS SET LAST_LOGIN_DATE = SYSDATE, LOGIN_IP = #{ip} WHERE USER_ID
// = #{id}
// 회원 테이블 관리 차원에서 중요합니다.
// 추천: 가장 시급한 것은 1번 인터셉터입니다. 이게 없으면 기껏 만든 로그인이 무용지물이 될 수 있습니다. 혹시 인터셉터 설정도 진행하실
// 예정인가요?

// 2. 아이디 저장 (Cookie)
// 상황: 사용자가 매번 아이디를 치기 귀찮아합니다.
// 해결: 로그인 화면의 [v] 아이디 저장 체크박스 기능입니다. 로그인 성공 시 **쿠키(Cookie)**에 아이디를 암호화 없이
// 저장해두고, 다음 접속 시
// Controller
// 에서 쿠키를 확인해 아이디 입력창을 미리 채워줍니다.
// 이거계속껏다켜는서버인데 가능한거야? 보안상으로도괜찮고?
