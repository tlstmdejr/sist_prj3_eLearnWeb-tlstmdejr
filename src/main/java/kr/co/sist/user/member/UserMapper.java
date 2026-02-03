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
     * 회원가입
     * 
     * @param userId 사용자 아이디
     * @return StudentDomain 사용자 정보
     * @throws PersistenceException DB 예외
     */
    public int insertUser(UserDTO uDTO) throws PersistenceException;

    /**
     * 아이디 중복 확인
     * 
     * @param id 확인할 아이디
     * @return String 사용중인 아이디 (없으면 null)
     * @throws PersistenceException DB 예외
     */
    public String selectId(String id) throws PersistenceException;

    /**
     * 이름 중복 확인
     * 
     * @param name 확인할 이름
     * @return String 사용중인 이름 (없으면 null)
     * @throws PersistenceException DB 예외
     */
    public String selectName(String name) throws PersistenceException;

}
// interface
