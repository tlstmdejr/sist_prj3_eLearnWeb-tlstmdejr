package kr.co.sist.user.my.setting;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;

@Mapper
public interface SettingMapper {

    // 설정 페이지 정보 조회
    // <select id = "selectSettingInfo" parameterType = "String" resultType =
    // "settingDomain">
    public SettingDomain selectSettingInfo(String userId) throws PersistenceException;

    // 프로필 이미지 변경
    // <update id = "updateImg" parameterType = "String, String">
    public int updateImg(@Param("userId") String userId, @Param("imgPath") String imgPath) throws PersistenceException;

    // 닉네임 변경
    // <update id = "updateNick" parameterType = "String, String">
    public int updateNick(@Param("userId") String userId, @Param("name") String name) throws PersistenceException;

    // 자기소개 변경
    // <update id = "updateIntro" parameterType = "String, String">
    public int updateIntro(@Param("userId") String userId, @Param("intro") String intro) throws PersistenceException;

    // 이메일 변경
    // <update id = "updateEmail" parameterType = "String, String">
    public int updateEmail(@Param("userId") String userId, @Param("email") String email) throws PersistenceException;

    // 비밀번호 조회 (현재 비밀번호 확인용)
    // <select id = "selectPassword" parameterType = "String" resultType = "String">
    public String selectPassword(String userId) throws PersistenceException;

    // 비밀번호 변경
    // <update id = "updatePass" parameterType = "String, String">
    public int updatePass(@Param("userId") String userId, @Param("password") String password)
            throws PersistenceException;

    // 휴대폰 번호 변경
    // <update id = "updatePhone" parameterType = "String, String">
    public int updatePhone(@Param("userId") String userId, @Param("phone") String phone) throws PersistenceException;

    // 회원 탈퇴 (비활성화)
    // <update id = "updateActivation" parameterType = "String">
    public int updateActivation(String userId) throws PersistenceException;
}
