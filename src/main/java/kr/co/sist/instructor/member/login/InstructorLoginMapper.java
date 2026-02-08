package kr.co.sist.instructor.member.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;
import kr.co.sist.instructor.member.InstructorDomain;

/**
 * 강사 - 로그인 Mapper Interface
 */
@Mapper
public interface InstructorLoginMapper {

    /**
     * 강사 정보 조회 (로그인용)
     * 
     * @param instId 강사 아이디
     * @return 강사 도메인
     * @throws PersistenceException
     */
    public InstructorDomain selectInstructor(String instId) throws PersistenceException;

}
