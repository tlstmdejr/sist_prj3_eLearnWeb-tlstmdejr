package kr.co.sist.user.my.profile;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import java.nio.charset.StandardCharsets;

import kr.co.sist.user.member.UserDomain;

/**
 * 내 프로필 관련 비즈니스 로직 Service
 * - 프로필 사진 & 닉네임 & 아이디 조회
 * - 자기소개 조회
 */
@Service
public class ProfileService {

    @Value("${user.crypto.key}")
    private String key;
    @Value("${user.crypto.salt}")
    private String salt;

    @Autowired
    private ProfileMapper pm;

    /**
     * 프로필 정보 조회
     * 
     * @param userId 사용자 아이디
     * @return UserDomain 프로필 정보
     */
    public UserDomain selectOneProfile(String userId) {
        UserDomain ud = null;

        try {
            // DB에서 프로필 정보 조회
            ud = pm.selectOneProfile(userId);

            if (ud != null) {
                // 암호화된 정보 복호화 (이름)
                TextEncryptor te = createEncryptor();
                ud.setName(te.decrypt(ud.getName()));
            }

        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        return ud;
    }

    /**
     * 결정적 암호화를 위한 Encryptor 생성 (검색 가능하도록 고정 IV 사용)
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
// class
