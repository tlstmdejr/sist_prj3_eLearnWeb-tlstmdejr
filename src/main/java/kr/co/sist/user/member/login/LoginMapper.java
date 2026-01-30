package kr.co.sist.user.member.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.sist.user.member.UserDomain;

/**
 * 학생 로그인 관련 MyBatis Mapper Interface
 * - XML의 namespace와 메서드명이 일치해야 함
 */
@Mapper
public interface LoginMapper {

    /**
     * 아이디로 사용자 정보 조회
     * @param userId 사용자 아이디
     * @return StudentDomain 사용자 정보
     * @throws PersistenceException DB 예외
     */
    public UserDomain selectOneUserInfo(String userId) throws PersistenceException;
}
// interface
