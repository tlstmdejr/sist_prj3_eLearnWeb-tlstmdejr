package kr.co.sist.user.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;


/**
 * 유저 관련 MyBatis Mapper Interface
 * - XML의 namespace와 메서드명이 일치해야 함
 */
@Mapper
public interface UserMapper {

    /**
     * 아이디로 사용자 정보 조회
     * @param userId 사용자 아이디
     * @return StudentDomain 사용자 정보
     * @throws PersistenceException DB 예외
     */
    public UserDomain selectOneUserInfo(String userId) throws PersistenceException;
}
// interface










