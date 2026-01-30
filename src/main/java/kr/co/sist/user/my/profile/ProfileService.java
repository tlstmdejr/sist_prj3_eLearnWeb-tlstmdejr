package kr.co.sist.user.my.profile;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.user.member.UserDomain;

/**
 * 내 프로필 관련 비즈니스 로직 Service
 * - 프로필 사진 & 닉네임 & 아이디 조회
 * - 자기소개 조회
 */
@Service
public class ProfileService {

    @Autowired
    private ProfileDAO pDAO;

    /**
     * 프로필 정보 조회
     * - 프로필 사진, 닉네임, 아이디, 자기소개 조회
     * @param userId 사용자 아이디
     * @return StudentDomain 프로필 정보
     */
    public UserDomain getProfile(String userId) {
        UserDomain sd = null;
        
        try {
            // DB에서 프로필 정보 조회
            sd = pDAO.selectProfile(userId);
            
            // 암호화된 정보 복호화 (필요시)
            // TextEncryptor te = Encryptors.text(key, salt);
            // sd.setName(te.decrypt(sd.getName()));
            
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        
        return sd;
    }
}
// class
