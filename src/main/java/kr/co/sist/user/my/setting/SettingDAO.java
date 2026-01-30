package kr.co.sist.user.my.setting;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.sist.dao.MyBatisHandler;

/**
 * 내 정보 설정 관련 DB 접근 DAO
 * - 프로필 사진 & 닉네임 & 자기소개 변경
 * - 이메일 조회 및 변경
 * - 비밀번호 조회 및 변경
 * - 휴대폰 번호 조회 및 변경
 */
@Repository
public class SettingDAO {

    // ===========================
    // 조회 (SELECT)
    // ===========================

    /**
     * 설정 페이지 정보 조회
     * @param userId 사용자 아이디
     * @return SettingDomain 설정 정보
     * @throws PersistenceException DB 예외
     */
    public SettingDomain selectSettingInfo(String userId) throws PersistenceException {
        SettingDomain sd = null;
        
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        // TODO: mapper 호출
        // sd = ss.selectOne("kr.co.sist.user.my.setting.selectSettingInfo", userId);
        if (ss != null) { ss.close(); }
        
        return sd;
    }

    /**
     * 비밀번호 조회 (비밀번호 변경 시 검증용)
     * @param userId 사용자 아이디
     * @return String 암호화된 비밀번호
     * @throws PersistenceException DB 예외
     */
    public String selectPassword(String userId) throws PersistenceException {
        String password = null;
        
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        // TODO: mapper 호출
        // password = ss.selectOne("kr.co.sist.user.my.setting.selectPassword", userId);
        if (ss != null) { ss.close(); }
        
        return password;
    }

    // ===========================
    // 수정 (UPDATE) - 프로필 정보
    // ===========================

    /**
     * 프로필 이미지 변경
     * @param userId 사용자 아이디
     * @param imgPath 새 이미지 경로
     * @return int 수정된 행 수
     * @throws PersistenceException DB 예외
     */
    public int updateImg(String userId, String imgPath) throws PersistenceException {
        int result = 0;
        
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        Map<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("imgPath", imgPath);
        // TODO: mapper 호출
        // result = ss.update("kr.co.sist.user.my.setting.updateImg", param);
        if (ss != null) { ss.close(); }
        
        return result;
    }

    /**
     * 닉네임 변경
     * @param userId 사용자 아이디
     * @param name 새 닉네임
     * @return int 수정된 행 수
     * @throws PersistenceException DB 예외
     */
    public int updateNick(String userId, String name) throws PersistenceException {
        int result = 0;
        
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        Map<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("name", name);
        // TODO: mapper 호출
        // result = ss.update("kr.co.sist.user.my.setting.updateNick", param);
        if (ss != null) { ss.close(); }
        
        return result;
    }

    /**
     * 자기소개 변경
     * @param userId 사용자 아이디
     * @param intro 새 자기소개
     * @return int 수정된 행 수
     * @throws PersistenceException DB 예외
     */
    public int updateIntro(String userId, String intro) throws PersistenceException {
        int result = 0;
        
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        Map<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("intro", intro);
        // TODO: mapper 호출
        // result = ss.update("kr.co.sist.user.my.setting.updateIntro", param);
        if (ss != null) { ss.close(); }
        
        return result;
    }

    // ===========================
    // 수정 (UPDATE) - 이메일, 비밀번호, 휴대폰
    // ===========================

    /**
     * 이메일 변경
     * @param userId 사용자 아이디
     * @param email 새 이메일
     * @return int 수정된 행 수
     * @throws PersistenceException DB 예외
     */
    public int updateEmail(String userId, String email) throws PersistenceException {
        int result = 0;
        
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        Map<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("email", email);
        // TODO: mapper 호출
        // result = ss.update("kr.co.sist.user.my.setting.updateEmail", param);
        if (ss != null) { ss.close(); }
        
        return result;
    }

    /**
     * 비밀번호 변경
     * @param userId 사용자 아이디
     * @param password 새 비밀번호 (암호화된 상태)
     * @return int 수정된 행 수
     * @throws PersistenceException DB 예외
     */
    public int updatePass(String userId, String password) throws PersistenceException {
        int result = 0;
        
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        Map<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("password", password);
        // TODO: mapper 호출
        // result = ss.update("kr.co.sist.user.my.setting.updatePass", param);
        if (ss != null) { ss.close(); }
        
        return result;
    }

    /**
     * 휴대폰 번호 변경
     * @param userId 사용자 아이디
     * @param phone 새 휴대폰 번호
     * @return int 수정된 행 수
     * @throws PersistenceException DB 예외
     */
    public int updatePhone(String userId, String phone) throws PersistenceException {
        int result = 0;
        
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        Map<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("phone", phone);
        // TODO: mapper 호출
        // result = ss.update("kr.co.sist.user.my.setting.updatePhone", param);
        if (ss != null) { ss.close(); }
        
        return result;
    }
}
// class
