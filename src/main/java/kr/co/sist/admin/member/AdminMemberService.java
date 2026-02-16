package kr.co.sist.admin.member;

import java.util.Collections;
import java.util.List;
import kr.co.sist.user.member.UserDomain;
import kr.co.sist.instructor.member.InstructorDomain;
import kr.co.sist.common.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

/**
 * 관리자 - 회원/강사 조회/관리 Service
 */
@Slf4j
@Service
public class AdminMemberService {

    private final AdminMemberMapper adminMemberMapper;
    private final CryptoUtil cryptoUtil;

    public AdminMemberService(AdminMemberMapper adminMemberMapper, CryptoUtil cryptoUtil) {
        this.adminMemberMapper = adminMemberMapper;
        this.cryptoUtil = cryptoUtil;
    }

    // ===========================
    // 회원 조회
    // ===========================

    public List<UserDomain> getUserList() {
        return getUserList(1, 10);
    }

    public List<UserDomain> getUserList(int page, int size) {
        int startRow = (page - 1) * size + 1;
        int endRow = page * size;

        List<UserDomain> list = null;
        try {
            list = adminMemberMapper.selectUserListByPage(startRow, endRow);

            if (list != null) {
                for (UserDomain user : list) {
                    user.setEmail(cryptoUtil.decryptSafe(user.getEmail()));
                    user.setName(cryptoUtil.decryptSafe(user.getName()));
                }
            }
        } catch (PersistenceException pe) {
            log.error("회원 목록 조회 실패", pe);
        }
        return list != null ? list : Collections.emptyList();
    }

    public int getUserTotalCount() {
        int count = 0;
        try {
            count = adminMemberMapper.selectUserCount();
        } catch (PersistenceException pe) {
            log.error("회원 총 건수 조회 실패", pe);
        }
        return count;
    }

    public UserDomain getUser(String id) {
        UserDomain user = null;
        try {
            user = adminMemberMapper.selectUser(id);

            if (user != null) {
                user.setEmail(cryptoUtil.decryptSafe(user.getEmail()));
                user.setName(cryptoUtil.decryptSafe(user.getName()));
            }
        } catch (PersistenceException pe) {
            log.error("회원 조회 실패 - id: {}", id, pe);
        }
        return user;
    }

    // ===========================
    // 강사 조회
    // ===========================

    public List<InstructorDomain> getInstructorList() {
        return getInstructorList(1, 10);
    }

    public List<InstructorDomain> getInstructorList(int page, int size) {
        int startRow = (page - 1) * size + 1;
        int endRow = page * size;

        List<InstructorDomain> list = null;
        try {
            list = adminMemberMapper.selectInstructorListByPage(startRow, endRow);

            if (list != null) {
                for (InstructorDomain inst : list) {
                    inst.setEmail(cryptoUtil.decryptSafe(inst.getEmail()));
                    inst.setName(cryptoUtil.decryptSafe(inst.getName()));
                }
            }
        } catch (PersistenceException pe) {
            log.error("강사 목록 조회 실패", pe);
        }
        return list != null ? list : Collections.emptyList();
    }

    public int getInstructorTotalCount() {
        int count = 0;
        try {
            count = adminMemberMapper.selectInstructorCount();
        } catch (PersistenceException pe) {
            log.error("강사 총 건수 조회 실패", pe);
        }
        return count;
    }

    public InstructorDomain getInstructor(String instId) {
        InstructorDomain inst = null;
        try {
            inst = adminMemberMapper.selectInstructor(instId);

            if (inst != null) {
                inst.setEmail(cryptoUtil.decryptSafe(inst.getEmail()));
                inst.setName(cryptoUtil.decryptSafe(inst.getName()));
            }
        } catch (PersistenceException pe) {
            log.error("강사 조회 실패 - instId: {}", instId, pe);
        }
        return inst;
    }

    // ===========================
    // 강사 승인
    // ===========================

    public boolean approveInstructor(String instId) {
        int cnt = 0;
        try {
            cnt = adminMemberMapper.updateInstructorStatus(instId);
        } catch (PersistenceException pe) {
            log.error("강사 승인 실패 - instId: {}", instId, pe);
        }
        return cnt > 0;
    }

}
