package kr.co.sist.user.member;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.sist.common.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 학생 회원 관련 비즈니스 로직을 처리하는 Service 클래스
 * - 회원가입, 정보조회/수정, 아이디/비밀번호 찾기, 탈퇴 등
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private CryptoUtil cryptoUtil;

    @Autowired
    private UserMapper um;

    /**
     * 학생 회원가입 처리
     *
     * @param sDTO 회원가입 정보가 담긴 DTO
     * @return int 회원가입 결과 (1: 성공, 0: 실패)
     */
    public boolean addUser(UserDTO sDTO) {
        boolean flag = false;
        // 일방향 해시 : 비번
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder(10);
        sDTO.setPassword(bpe.encode(sDTO.getPassword()));

        // 암호화 - CryptoUtil 공통 유틸 사용
        sDTO.setName(cryptoUtil.encrypt(sDTO.getName()));
        String email = sDTO.getEmail();
        if (email != null && !email.isEmpty()) {
            sDTO.setEmail(cryptoUtil.encrypt(email));
        }
        try {
            um.insertUser(sDTO);
            flag = true;
        } catch (PersistenceException pe) {
            log.error("유저 회원가입 실패", pe);
        }
        return flag;
    }

    /**
     * 학생 아이디 중복확인 (AJAX)
     *
     * @param stuId 확인할 아이디
     * @return boolean 중복 여부 (true: 사용가능, false: 중복)
     */
    public boolean chkUserId(String stuId) {
        boolean isAvailable = false;
        try {
            String id = um.selectId(stuId);
            if (id == null) {
                isAvailable = true;
            }
        } catch (PersistenceException pe) {
            log.error("유저 아이디 중복확인 실패 - id: {}", stuId, pe);
        }
        return isAvailable;
    }

    /**
     * 학생 이름 중복확인 (AJAX)
     *
     * @param name 확인할 이름
     * @return boolean 중복 여부 (true: 사용가능, false: 중복)
     */
    public boolean chkUserName(String name) {
        boolean isAvailable = false;
        try {
            // 이름은 암호화되어 저장되므로 암호화 후 조회
            String encName = cryptoUtil.encrypt(name);

            String result = um.selectName(encName);
            if (result == null) {
                isAvailable = true;
            }
        } catch (PersistenceException pe) {
            log.error("유저 이름 중복확인 실패", pe);
        }
        return isAvailable;
    }

    /**
     * 학생 전화번호 중복확인 (AJAX)
     *
     * @param phone 확인할 전화번호
     * @return boolean 중복 여부 (true: 사용가능, false: 중복)
     */
    public boolean chkUserPhone(String phone) {
        boolean isAvailable = false;
        try {
            String result = um.selectPhone(phone);
            if (result == null) {
                isAvailable = true;
            }
        } catch (PersistenceException pe) {
            log.error("유저 전화번호 중복확인 실패", pe);
        }
        return isAvailable;
    }

}
