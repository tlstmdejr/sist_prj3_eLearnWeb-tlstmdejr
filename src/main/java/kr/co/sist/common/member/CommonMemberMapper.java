package kr.co.sist.common.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

/**
 * 공통 - 회원 멤버 Mapper Interface (아이디/비번 찾기)
 */
@Mapper
public interface CommonMemberMapper {

    /**
     * 전화번호로 사용자 ID 조회
     * 
     * @param phone 전화번호
     * @return 사용자 ID
     * @throws PersistenceException
     */
    public String selectUserByPhone(String phone) throws PersistenceException;

    /**
     * 전화번호로 강사 ID 조회
     * 
     * @param phone 전화번호
     * @return 강사 ID
     * @throws PersistenceException
     */
    public String selectInstructorByPhone(String phone) throws PersistenceException;

    /**
     * 문자 발송 이력 저장
     * 
     * @param params (phone, email)
     * @return 성공 여부
     * @throws PersistenceException
     */
    public int insertTransmissionHistory(java.util.Map<String, String> params) throws PersistenceException;

    /**
     * 사용자 비밀번호 변경
     * 
     * @param params (id, password)
     * @return 변경 행 수
     * @throws PersistenceException
     */
    public int updateUserPassword(java.util.Map<String, String> params) throws PersistenceException;

    /**
     * 강사 비밀번호 변경
     * 
     * @param params (id, password)
     * @return 변경 행 수
     * @throws PersistenceException
     */
    public int updateInstructorPassword(java.util.Map<String, String> params) throws PersistenceException;

}
