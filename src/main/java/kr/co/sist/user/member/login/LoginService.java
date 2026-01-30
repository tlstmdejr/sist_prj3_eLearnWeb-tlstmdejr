package kr.co.sist.user.member.login;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.sist.user.member.UserDTO;
import kr.co.sist.user.member.UserDomain;

/**
 * 학생 로그인 관련 비즈니스 로직 Service
 * - Mapper Interface를 사용하여 DB 접근
 * - 빈 이름을 명시적으로 지정하여 다른 모듈과의 충돌 방지
 */
@Service("userLoginService")
public class LoginService {

	@Autowired(required = false)
	private LoginMapper lMapper;  // DAO 대신 Mapper Interface 사용
	
	@Value("${user.crypto.key}")
	private String key;
	@Value("${user.crypto.salt}")
	private String salt;
	
	/**
	 * 학생 로그인 검증
	 * @param sDTO 로그인 정보 (id, password)
	 * @return StudentDomain 로그인 결과 (성공 시 사용자 정보, 실패 시 에러 메시지)
	 */
	public UserDomain loginUser(UserDTO sDTO) {
	    UserDomain sd = null;
	    
	    try {
	        // 1. 아이디로 사용자 조회 (Mapper Interface 사용)
	        sd = lMapper.selectOneUserInfo(sDTO.getId());
	        
	        if (sd == null) {
	            // 아이디가 존재하지 않음
	            sd = new UserDomain();
	            } else {
	                // 2. 비밀번호 검증 (BCrypt)
	                BCryptPasswordEncoder bce = new BCryptPasswordEncoder();
	                if (bce.matches(sDTO.getPassword(), sd.getPassword())) {
	                    // 로그인 성공
	                } else {
	                    // 비밀번호 불일치
	                    sd = null;
	                }
	            }
	        
	    } catch (PersistenceException pe) {
	        pe.printStackTrace();
	    }
	    
	    return sd;  // Controller에서 결과 확인 후 세션 처리
	}
}
// class
