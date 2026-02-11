package kr.co.sist.common.util;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * 공통 암호화/복호화 유틸리티
 *
 * 사용법:
 * - @Autowired private CryptoUtil cryptoUtil;
 * - cryptoUtil.encrypt("평문") → 암호화된 문자열
 * - cryptoUtil.decrypt("암호문") → 복호화된 문자열
 * - cryptoUtil.decryptSafe("암호문") → 복호화 실패 시 null 반환
 *
 * [주의사항]
 * - application.properties에 user.crypto.key, user.crypto.salt 설정 필수
 * - Fixed IV 방식 사용 (같은 평문 → 같은 암호문)
 */
@Component
public class CryptoUtil {

    // 암호화 키 (application.properties에서 주입) - 기본값 없음 (필수 설정)
    @Value("${user.crypto.key}")
    private String key;

    @Value("${user.crypto.salt}")
    private String salt;

    @PostConstruct
    public void init() {
        // 서버 시작 시 암호화 키 설정 확인용 로그
        System.out.println("[CryptoUtil] 암호화 키 로딩 완료 - key length: " + (key != null ? key.length() : "null")
                + ", salt length: " + (salt != null ? salt.length() : "null"));

        // 암/복호화 라운드트립 테스트
        try {
            String testPlain = "crypto_test";
            String encrypted = encrypt(testPlain);
            String decrypted = decrypt(encrypted);
            System.out.println("[CryptoUtil] 라운드트립 테스트: " + (testPlain.equals(decrypted) ? "성공" : "실패"));
        } catch (Exception e) {
            System.err.println("[CryptoUtil] 라운드트립 테스트 실패: " + e.getMessage());
        }
    }

    /**
     * Fixed IV 암호화기 생성
     * - 같은 평문을 암호화하면 항상 같은 암호문 생성
     * - 로그인 비밀번호 비교, 검색 등에 적합
     */
    private TextEncryptor createEncryptor() {
        return new TextEncryptor() {
            // 고정 IV를 사용하는 AES 암호화기
            final AesBytesEncryptor encryptor = new AesBytesEncryptor(key, salt, new BytesKeyGenerator() {
                @Override
                public int getKeyLength() {
                    return 16;
                }

                @Override
                public byte[] generateKey() {
                    return new byte[16]; // 모든 바이트가 0인 고정 IV
                }
            });

            @Override
            public String encrypt(String text) {
                // 평문 → 암호화 → Hex 인코딩
                return new String(Hex.encode(encryptor.encrypt(text.getBytes(StandardCharsets.UTF_8))));
            }

            @Override
            public String decrypt(String encryptedText) {
                // Hex 디코딩 → 복호화 → 평문
                return new String(encryptor.decrypt(Hex.decode(encryptedText)), StandardCharsets.UTF_8);
            }
        };
    }

    // ===========================
    // 공개 메서드
    // ===========================

    /**
     * 문자열 암호화
     *
     * @param plainText 암호화할 평문
     * @return 암호화된 문자열 (Hex 인코딩)
     */
    public String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }
        return createEncryptor().encrypt(plainText);
    }

    /**
     * 문자열 복호화
     * - 암호화되지 않은 평문이나 키 불일치 시 예외 발생
     *
     * @param encryptedText 복호화할 암호문
     * @return 복호화된 평문
     * @throws Exception 복호화 실패 시
     */
    public String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }
        return createEncryptor().decrypt(encryptedText);
    }

    /**
     * 안전한 문자열 복호화 (실패 시 null 반환)
     * - 복호화 실패해도 예외 발생 X
     * - 평문 데이터, 키 불일치 등 모든 경우에 안전하게 동작
     *
     * @param encryptedText 복호화할 암호문 (또는 평문)
     * @return 복호화된 평문, 실패 시 null 반환
     */
    public String decryptSafe(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }
        try {
            return createEncryptor().decrypt(encryptedText);
        } catch (Exception e) {
            // 복호화 실패 시 null 반환 (평문이거나 키 불일치)
            System.err.println("[CryptoUtil] 복호화 실패 - key length: " + key.length()
                    + ", 입력 데이터 길이: " + encryptedText.length()
                    + ", 에러: " + e.getMessage());
            return null;
        }
    }

}