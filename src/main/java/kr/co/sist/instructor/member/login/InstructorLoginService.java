package kr.co.sist.instructor.member.login;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;
import kr.co.sist.instructor.member.InstructorDTO;
import kr.co.sist.instructor.member.InstructorDomain;

/**
 * 강사 - 로그인 서비스
 */
@Service
public class InstructorLoginService {

    private final InstructorLoginMapper instructorLoginMapper;

    public InstructorLoginService(InstructorLoginMapper instructorLoginMapper) {
        this.instructorLoginMapper = instructorLoginMapper;
    }

    /**
     * 강사 로그인
     * 
     * @param iDTO 로그인 정보
     * @return 로그인 성공 시 강사 도메인, 실패 시 null
     */
    public InstructorDomain login(InstructorDTO iDTO) {
        InstructorDomain iDomain = null;
        try {
            InstructorDomain tempDomain = instructorLoginMapper.selectInstructor(iDTO.getInstId());
            if (tempDomain != null) {
                // 비밀번호 확인 (BCrypt)
                org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

                boolean isMatch = encoder.matches(iDTO.getPassword(), tempDomain.getPassword());
                if (!isMatch) {
                    // BCrypt 매칭 실패 시 평문 비교 시도 (테스트 데이터 지원)
                    isMatch = iDTO.getPassword().equals(tempDomain.getPassword());
                }

                if (isMatch) {
                    // 승인 여부 및 활성화 여부 확인
                    if (tempDomain.getApproval() == 1 && tempDomain.getActivation() == 1) {
                        iDomain = tempDomain;
                    }
                }
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return iDomain;
    }

}
