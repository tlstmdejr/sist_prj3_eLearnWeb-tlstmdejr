package kr.co.sist.member.login;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import kr.co.sist.member.MemberDomain;

@Service
public class LoginService {

	@Autowired(required = false)
	private LoginDAO lDAO;
	
	@Value("${user.crypto.key}")
	private String key;
	@Value("${user.crypto.salt}")
	private String salt;
	
	public void insertMemberHistory(LoginDTO lDTO) {
	
		try {
			lDAO.insertLoginHistory(lDTO);
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
	}
	
	public MemberDomain searchOneMember(LoginDTO lDTO){
		MemberDomain md=null;
		try {
			md=lDAO.selectOneMember(lDTO);

			if(md==null) {
				md=new MemberDomain();
				md.setResultMsg("아이디가 존재하지 않습니다");
			}else {
				BCryptPasswordEncoder bce=new BCryptPasswordEncoder();
				if(bce.matches(lDTO.getPassword(), md.getPassword())) {
					md.setResultMsg("로그인성공");
					lDTO.setResult("s");
					//복호화
					TextEncryptor te=Encryptors.text(key, salt);
					md.setName(te.decrypt(md.getName()));
					
					String email=md.getEmail();
					if(email != null && !"".equals(email)) {
						md.setEmail(te.decrypt(email));
					}
				}else {
					md.setResultMsg("로그인실패");
					lDTO.setResult("F");
				}
				insertMemberHistory(lDTO);
			}//end if
		
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		return md;
	}
	
}
//class
