package kr.co.sist.user.my.profile;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.sist.dao.MyBatisHandler;
import kr.co.sist.user.member.UserDomain;

/**
 * 내 프로필 관련 DB 접근 DAO
 * - 프로필 사진 & 닉네임 & 아이디 조회
 * - 자기소개 조회
 */
@Repository
public class ProfileDAO {

    /**
     * 프로필 정보 조회
     * - 프로필 사진, 닉네임, 아이디, 자기소개 조회
     * @param userId 사용자 아이디
     * @return StudentDomain 프로필 정보
     * @throws PersistenceException DB 예외
     */
    public UserDomain selectProfile(String userId) throws PersistenceException {
        UserDomain sd = null;
        
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        // TODO: mapper 호출
        // sd = ss.selectOne("kr.co.sist.user.my.profile.selectProfile", userId);
        if (ss != null) { ss.close(); }
        
        return sd;
    }
}
// class
