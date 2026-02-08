package kr.co.sist.user.my.profile;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.sist.user.member.UserDomain;

/**
 * 학생 로그인 관련 MyBatis Mapper Interface
 * - XML의 namespace와 메서드명이 일치해야 함
 */
@Mapper
public interface ProfileMapper {

    // 아이디로 사용자 정보 조회
    // <select id = "selectOneProfile" parameterType = "String" resultType =
    // "userDomain">
    public UserDomain selectOneProfile(String userId) throws PersistenceException;
}
// interface
