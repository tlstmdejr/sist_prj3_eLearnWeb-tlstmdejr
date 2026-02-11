package kr.co.sist.admin.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;
import kr.co.sist.admin.member.AdminDomain;

/**
 * 관리자 - 로그인 Mapper Interface
 */
@Mapper
public interface AdminLoginMapper {

    /**
     * 관리자 정보 조회 (로그인용)
     * 
     * @param adminId 관리자 아이디
     * @return 관리자 도메인
     * @throws PersistenceException
     */
    public AdminDomain selectAdmin(String adminId) throws PersistenceException;

}
