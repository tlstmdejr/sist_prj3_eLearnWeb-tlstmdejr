package kr.co.sist.user.member.login;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.sist.user.member.StudentDTO;
import kr.co.sist.user.member.StudentDomain;

@Service
public class LoginService {

	@Autowired(required = false)
	private LoginDAO lDAO;
	
	@Value("${user.crypto.key}")
	private String key;
	@Value("${user.crypto.salt}")
	private String salt;
	
	/**
 * 학생 로그인 검증
 * @param sDTO 로그인 정보 (id, password)
 * @return StudentDomain 로그인 결과 (성공 시 사용자 정보, 실패 시 에러 메시지)
 */
public StudentDomain loginUser(StudentDTO sDTO) {
    StudentDomain sd = null;
    
    try {
        // 1. 아이디로 사용자 조회
        sd = lDAO.selectOneStu(sDTO.getId());
        
        if (sd == null) {
            // 아이디가 존재하지 않음
            sd = new StudentDomain();
        } else {
            // 2. 비밀번호 검증 (BCrypt)
            BCryptPasswordEncoder bce = new BCryptPasswordEncoder();
            if (bce.matches(sDTO.getPassword(), sd.getPassword())) {
                
                // 3. 암호화된 개인정보 복호화 (필요시)
                // TextEncryptor te = Encryptors.text(key, salt);
                // sd.setName(te.decrypt(sd.getName()));
                
            } else {
            }
        }
        
    } catch (PersistenceException pe) {
        pe.printStackTrace();
    }
    
    return sd;  // Controller에서 결과 확인 후 세션 처리
}
	
}
//class
