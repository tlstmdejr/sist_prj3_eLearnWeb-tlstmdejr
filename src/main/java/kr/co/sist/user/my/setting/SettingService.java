package kr.co.sist.user.my.setting;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import kr.co.sist.common.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 내 정보 설정 관련 비즈니스 로직 Service
 * - 프로필 사진 & 닉네임 & 자기소개 변경
 * - 이메일 조회 및 변경
 * - 비밀번호 조회 및 변경
 * - 휴대폰 번호 조회 및 변경
 *
 * [수정사항]
 * - CryptoUtil 공통 유틸리티 사용으로 암호화 키 불일치 방지
 */
@Slf4j
@Service
public class SettingService {

    @Autowired
    private SettingMapper sm;

    @Autowired
    private CryptoUtil cryptoUtil;

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

            if (sd != null) {
                // 암호화된 정보 복호화 - CryptoUtil.decryptSafe 사용 (실패 시 null 반환)
                sd.setEmail(cryptoUtil.decryptSafe(sd.getEmail()));
                sd.setName(cryptoUtil.decryptSafe(sd.getName()));
            }

        } catch (PersistenceException pe) {
            log.error("설정 정보 조회 실패 - userId: {}", userId, pe);
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
            log.error("프로필 이미지 변경 실패 - userId: {}", userId, pe);
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
            name = cryptoUtil.encrypt(name);
            result = sm.updateNick(userId, name);
        } catch (PersistenceException pe) {
            log.error("닉네임 변경 실패 - userId: {}", userId, pe);
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
            log.error("자기소개 변경 실패 - userId: {}", userId, pe);
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
     * @return int 수정 결과 (1: 성공, 0: 실패, -1: 같은 이메일)
     */
    public int modifyEmail(String userId, String email) {
        int result = 0;

        try {
            // 현재 이메일 조회 및 복호화
            SettingDomain currentInfo = sm.selectSettingInfo(userId);
            if (currentInfo != null && currentInfo.getEmail() != null) {
                String currentEmail = cryptoUtil.decryptSafe(currentInfo.getEmail());

                // 같은 이메일인 경우
                if (email.equals(currentEmail)) {
                    return -1; // same_email
                }
            }

            // 새 이메일 암호화 후 저장
            String encryptedEmail = cryptoUtil.encrypt(email);
            result = sm.updateEmail(userId, encryptedEmail);
        } catch (PersistenceException pe) {
            log.error("이메일 변경 실패 - userId: {}", userId, pe);
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
            log.error("비밀번호 변경 실패 - userId: {}", userId, pe);
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
            log.error("휴대폰 번호 변경 실패 - userId: {}", userId, pe);
        }

        return result;
    }

    /**
     * 회원 탈퇴 (비활성화)
     *
     * @param userId
     * @return
     */
    public int withdrawalUser(String userId) {
        int result = 0;
        try {
            result = sm.updateActivation(userId);
        } catch (PersistenceException pe) {
            log.error("회원 탈퇴 처리 실패 - userId: {}", userId, pe);
        }
        return result;
    }

}
