package kr.co.sist.admin.member;

import java.util.List;
import kr.co.sist.user.member.UserDomain;
import kr.co.sist.instructor.member.InstructorDomain;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

/**
 * 관리자 - 회원 관리 서비스
 */
@Service
public class AdminMemberService {

    private final AdminMemberMapper adminMemberMapper;

    public AdminMemberService(AdminMemberMapper adminMemberMapper) {
        this.adminMemberMapper = adminMemberMapper;
    }

    /**
     * 사용자 목록 조회
     * 
     * @return 사용자 리스트
     */
    public List<UserDomain> getUserList() {
        List<UserDomain> list = null;
        try {
            list = adminMemberMapper.selectUserList();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return list;
    }

    /**
     * 강사 목록 조회
     * 
     * @return 강사 리스트
     */
    public List<InstructorDomain> getInstructorList() {
        List<InstructorDomain> list = null;
        try {
            list = adminMemberMapper.selectInstructorList();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return list;
    }

    /**
     * 강사 승인 처리
     * 
     * @param instId 승인할 강사 아이디
     * @return 성공 여부
     */
    public boolean approveInstructor(String instId) {
        int cnt = 0;
        try {
            cnt = adminMemberMapper.updateInstructorStatus(instId);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return cnt > 0;
    }

}
