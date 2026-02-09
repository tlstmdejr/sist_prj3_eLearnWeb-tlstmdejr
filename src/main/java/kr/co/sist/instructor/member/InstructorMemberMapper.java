package kr.co.sist.instructor.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;
import kr.co.sist.instructor.member.InstructorDomain;

/**
 * 강사 - 회원가입(Member) Mapper Interface
 */
@Mapper
public interface InstructorMemberMapper {

    /**
     * 강사 등록
     * 
     * @param instructorDomain 강사 도메인 객체
     * @return 등록 성공 여부
     * @throws PersistenceException
     */
    public int insertInstructor(InstructorDomain instructorDomain) throws PersistenceException;

    /**
     * 아이디 중복 확인
     * 
     * @param id 아이디
     * @return 아이디 (없으면 null)
     * @throws PersistenceException
     */
    public String selectId(String id) throws PersistenceException;

}
