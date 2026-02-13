package kr.co.sist.user.my.profile;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.common.util.CryptoUtil;
import kr.co.sist.user.member.UserDomain;
import lombok.extern.slf4j.Slf4j;

/**
 * 내 프로필 관련 비즈니스 로직 Service
 * - 프로필 사진 & 닉네임 & 아이디 조회
 * - 자기소개 조회
 */
@Slf4j
@Service
public class ProfileService {

    @Autowired
    private CryptoUtil cryptoUtil;

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
                // 암호화된 정보 복호화 (이름, 이메일)
                ud.setName(cryptoUtil.decryptSafe(ud.getName()));
                ud.setEmail(cryptoUtil.decryptSafe(ud.getEmail()));
            }

        } catch (PersistenceException pe) {
            log.error("프로필 조회 실패 - userId: {}", userId, pe);
        }

        return ud;
    }
}
