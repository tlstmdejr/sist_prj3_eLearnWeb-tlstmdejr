package kr.co.sist.user.my.setting;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;

/**
 * 내 정보 설정 관련 비즈니스 로직 Service
 * - 프로필 사진 & 닉네임 & 자기소개 변경
 * - 이메일 조회 및 변경
 * - 비밀번호 조회 및 변경
 * - 휴대폰 번호 조회 및 변경
 * 
 * [수정사항]
 * - UserService와 동일한 암호화 방식(Fixed IV)을 사용하여 상호 호환성 확보
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
            TextEncryptor te = createEncryptor();
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
            TextEncryptor te = createEncryptor();
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
            TextEncryptor te = createEncryptor();
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

    /**
     * 결정적 암호화를 위한 Encryptor 생성 (UserService와 동일 로직)
     * - 검색 및 중복 확인을 위해 고정된 IV(Initialization Vector)를 사용
     */
    private TextEncryptor createEncryptor() {
        return new TextEncryptor() {
            // 고정 IV 사용 (0으로 초기화된 16바이트)
            private final AesBytesEncryptor encryptor = new AesBytesEncryptor(key, salt, new BytesKeyGenerator() {
                @Override
                public int getKeyLength() {
                    return 16;
                }

                @Override
                public byte[] generateKey() {
                    return new byte[16];
                }
            });

            @Override
            public String encrypt(String text) {
                return new String(Hex.encode(encryptor.encrypt(text.getBytes(StandardCharsets.UTF_8))));
            }

            @Override
            public String decrypt(String encryptedText) {
                return new String(encryptor.decrypt(Hex.decode(encryptedText)), StandardCharsets.UTF_8);
            }
        };
    }
}
