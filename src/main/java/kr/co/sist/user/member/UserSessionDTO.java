package kr.co.sist.user.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 공통부분 : 세션에 저장할 핵심정보(아이디, 이름, 권한)
 * - 로그인, 아이디/비밀번호 찾기, 회원탈퇴 등은 페이지가 같아 
 *   Service에서 같은 메소드를 사용하기 때문
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserSessionDTO {

    private String id;      // 사용자 아이디
    private String pass;    // 사용자 비밀번호
    private String name;    // 사용자 이름
    private String email;   // 사용자 이메일
    private String phone;   // 사용자 전화번호
    private String status;  // 사용자 상태 (활성/휴면 등)
    
}
// class
