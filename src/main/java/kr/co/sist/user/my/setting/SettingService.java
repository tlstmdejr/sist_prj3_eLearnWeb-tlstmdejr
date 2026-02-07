package kr.co.sist.user.my.setting;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

/**
 * 내 정보 설정 관련 비즈니스 로직 Service
 * - 프로필 사진 & 닉네임 & 자기소개 변경
 * - 이메일 조회 및 변경
 * - 비밀번호 조회 및 변경
 * - 휴대폰 번호 조회 및 변경
 */
@Service
public class SettingService {

    @Autowired
    private SettingMapper sm;

    // 암호화 키 (application.properties에서 주입)
    @Value("${user.crypto.key:defaultKey}")
    private String key;

    @Value("${user.crypto.salt:defaultSalt}")
    private String salt;

    // ===========================
    // 정보 조회
    // ===========================

    /**
     * 설정 페이지 정보 조회
     * - 프로필 사진, 닉네임, 아이디, 자기소개
     * - 이메일, 휴대폰 번호 조회
     * 
     * @param userId 사용자 아이디
     * @return SettingDomain 설정 정보
     */
    public SettingDomain getSettingInfo(String userId) {
        SettingDomain sd = null;

        try {
            sd = sm.selectSettingInfo(userId);

            // 암호화된 정보 복호화
            TextEncryptor te = Encryptors.text(key, salt);
            sd.setEmail(te.decrypt(sd.getEmail()));
            sd.setName(te.decrypt(sd.getName()));

        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        return sd;
    }

    // ===========================
    // 프로필 정보 수정
    // ===========================

    /**
     * 프로필 이미지 변경
     * 
     * @param userId  사용자 아이디
     * @param imgPath 새 이미지 경로
     * @return int 수정 결과 (1: 성공, 0: 실패)
     */
    public int modifyImg(String userId, String imgPath) {
        int result = 0;

        try {
            result = sm.updateImg(userId, imgPath);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        return result;
    }

    /**
     * 닉네임 변경
     * 
     * @param userId 사용자 아이디
     * @param name   새 닉네임
     * @return int 수정 결과 (1: 성공, 0: 실패)
     */
    public int modifyNick(String userId, String name) {
        int result = 0;

        try {
            TextEncryptor te = Encryptors.text(key, salt);
            name = te.encrypt(name);
            result = sm.updateNick(userId, name);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        return result;
    }

    /**
     * 자기소개 변경
     * 
     * @param userId 사용자 아이디
     * @param intro  새 자기소개
     * @return int 수정 결과 (1: 성공, 0: 실패)
     */
    public int modifyIntro(String userId, String intro) {
        int result = 0;

        try {
            result = sm.updateIntro(userId, intro);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        return result;
    }

    // ===========================
    // 이메일, 비밀번호, 휴대폰 수정
    // ===========================

    /**
     * 이메일 변경
     * 
     * @param userId 사용자 아이디
     * @param email  새 이메일
     * @return int 수정 결과 (1: 성공, 0: 실패)
     */
    public int modifyEmail(String userId, String email) {
        int result = 0;

        try {
            // 이메일 암호화
            TextEncryptor te = Encryptors.text(key, salt);
            email = te.encrypt(email);

            result = sm.updateEmail(userId, email);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        return result;
    }

    /**
     * 비밀번호 변경
     * 
     * @param userId      사용자 아이디
     * @param currentPass 현재 비밀번호
     * @param newPass     새 비밀번호
     * @return int 수정 결과 (1: 성공, 0: 실패, -1: 현재 비밀번호 불일치)
     */
    public int modifyPass(String userId, String currentPass, String newPass) {
        int result = 0;

        try {
            // 1. 현재 비밀번호 확인
            String dbPass = sm.selectPassword(userId);
            BCryptPasswordEncoder bce = new BCryptPasswordEncoder();

            if (!bce.matches(currentPass, dbPass)) {
                return -1; // 현재 비밀번호 불일치
            }

            // 2. 새 비밀번호 암호화 후 저장
            String encodedPass = bce.encode(newPass);
            result = sm.updatePass(userId, encodedPass);

        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        return result;
    }

    /**
     * 휴대폰 번호 변경
     * 
     * @param userId 사용자 아이디
     * @param phone  새 휴대폰 번호
     * @return int 수정 결과 (1: 성공, 0: 실패)
     */
    public int modifyPhone(String userId, String phone) {
        int result = 0;

        try {
            result = sm.updatePhone(userId, phone);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        return result;
    }
}
