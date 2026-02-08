package kr.co.sist.user.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

/**
 * 유저 관련 MyBatis Mapper Interface
 * - XML의 namespace와 메서드명이 일치해야 함
 */
@Mapper
public interface UserMapper {

    // 회원가입
    // <insert id = "insertUser" parameterType = "userDTO">
    public int insertUser(UserDTO uDTO) throws PersistenceException;

    // 아이디 중복 확인
    // <select id = "selectId" parameterType = "String" resultType = "String">
    public String selectId(String id) throws PersistenceException;

    // 이름 중복 확인
    // <select id = "selectName" parameterType = "String" resultType = "String">
    public String selectName(String name) throws PersistenceException;

    // 전화번호 중복 확인
    // <select id = "selectPhone" parameterType = "String" resultType = "String">
    public String selectPhone(String phone) throws PersistenceException;

}
// interface
