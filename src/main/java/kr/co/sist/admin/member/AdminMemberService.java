package kr.co.sist.admin.member;

import java.util.List;
import kr.co.sist.user.member.UserDomain;
import kr.co.sist.instructor.member.InstructorDomain;
import kr.co.sist.common.util.CryptoUtil;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

/**
 * 관리자 - 회원 관리 서비스
 * 
 * [암호화/복호화]
 * - 회원/강사의 이메일, 닉네임은 DB에 암호화되어 저장됨
 * - 관리자 페이지에서 조회 시 복호화하여 표시
 * - CryptoUtil 공통 유틸리티 사용
 */
@Service
public class AdminMemberService {

    private final AdminMemberMapper adminMemberMapper;
    private final CryptoUtil cryptoUtil;

    public AdminMemberService(AdminMemberMapper adminMemberMapper, CryptoUtil cryptoUtil) {
        this.adminMemberMapper = adminMemberMapper;
        this.cryptoUtil = cryptoUtil;
    }

    // ===========================
    // 회원 관리
    // ===========================

    /**
     * 사용자 목록 조회 (이메일, 닉네임 복호화)
     * 
     * @return 사용자 리스트
     */
    public List<UserDomain> getUserList() {
        List<UserDomain> list = null;
        try {
            list = adminMemberMapper.selectUserList();

            // 암호화된 정보 복호화
            for (UserDomain user : list) {
                user.setEmail(cryptoUtil.decryptSafe(user.getEmail()));
                user.setName(cryptoUtil.decryptSafe(user.getName()));
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return list;
    }

    /**
     * 사용자 개별 조회 (이메일, 닉네임 복호화)
     * 
     * @param id 사용자 아이디
     * @return 사용자 정보
     */
    public UserDomain getUser(String id) {
        UserDomain user = null;
        try {
            user = adminMemberMapper.selectUser(id);

            // 암호화된 정보 복호화
            if (user != null) {
                user.setEmail(cryptoUtil.decryptSafe(user.getEmail()));
                user.setName(cryptoUtil.decryptSafe(user.getName()));
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return user;
    }

    // ===========================
    // 강사 관리
    // ===========================

    /**
     * 강사 목록 조회 (이메일, 닉네임 복호화)
     * 
     * @return 강사 리스트
     */
    public List<InstructorDomain> getInstructorList() {
        List<InstructorDomain> list = null;
        try {
            list = adminMemberMapper.selectInstructorList();

            // 암호화된 정보 복호화
            for (InstructorDomain inst : list) {
                inst.setEmail(cryptoUtil.decryptSafe(inst.getEmail()));
                inst.setName(cryptoUtil.decryptSafe(inst.getName()));
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return list;
    }

    /**
     * 강사 개별 조회 (이메일, 닉네임 복호화)
     * 
     * @param instId 강사 아이디
     * @return 강사 정보
     */
    public InstructorDomain getInstructor(String instId) {
        InstructorDomain inst = null;
        try {
            inst = adminMemberMapper.selectInstructor(instId);

            // 암호화된 정보 복호화
            if (inst != null) {
                inst.setEmail(cryptoUtil.decryptSafe(inst.getEmail()));
                inst.setName(cryptoUtil.decryptSafe(inst.getName()));
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return inst;
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
