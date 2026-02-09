package kr.co.sist.admin.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;
import java.util.List;
import kr.co.sist.user.member.UserDomain;
import kr.co.sist.instructor.member.InstructorDomain;

/**
 * 관리자 - 회원 관리 Mapper Interface
 */
@Mapper
public interface AdminMemberMapper {

    /**
     * 사용자 목록 조회
     * 
     * @return 사용자 리스트
     * @throws PersistenceException
     */
    public List<UserDomain> selectUserList() throws PersistenceException;

    /**
     * 사용자 개별 조회
     * 
     * @param id 사용자 아이디
     * @return 사용자 정보
     * @throws PersistenceException
     */
    public UserDomain selectUser(String id) throws PersistenceException;

    /**
     * 강사 목록 조회
     * 
     * @return 강사 리스트
     * @throws PersistenceException
     */
    public List<InstructorDomain> selectInstructorList() throws PersistenceException;

    /**
     * 강사 개별 조회
     * 
     * @param instId 강사 아이디
     * @return 강사 정보
     * @throws PersistenceException
     */
    public InstructorDomain selectInstructor(String instId) throws PersistenceException;

    /**
     * 강사 승인 상태 변경
     * 
     * @param instId 강사 아이디
     * @return 변경된 행의 수
     * @throws PersistenceException
     */
    public int updateInstructorStatus(String instId) throws PersistenceException;

}
