package kr.co.sist.instructor.member;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;
import kr.co.sist.instructor.member.InstructorDTO;
import kr.co.sist.instructor.member.InstructorDomain;
import kr.co.sist.instructor.member.InstructorMemberMapper;

/**
 * 강사 - 회원가입(Member) 서비스
 */
@Service
public class InstructorService {

    private final InstructorMemberMapper instructorMemberMapper;

    public InstructorService(InstructorMemberMapper instructorMemberMapper) {
        this.instructorMemberMapper = instructorMemberMapper;
    }

    public boolean addInstructor(InstructorDTO iDTO) {
        // 비밀번호 암호화
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        iDTO.setPassword(encoder.encode(iDTO.getPassword()));

        // DTO -> Domain 변환
        InstructorDomain iDomain = new InstructorDomain();
        iDomain.setInstId(iDTO.getInstId());
        iDomain.setPassword(iDTO.getPassword());
        iDomain.setName(iDTO.getName());
        iDomain.setPhone(iDTO.getPhone());
        iDomain.setEmail(iDTO.getEmail());
        iDomain.setBirth(iDTO.getBirth());
        iDomain.setProfile(iDTO.getProfile());
        // IP는 Controller에서? 아니면 여기서? Controller에서 받아와야 함. 일단 null 처리하거나 추가.
        // DTO에 regip가 없으므로... Controller에서 넣어서 DTO에 추가하거나 매개변수로 받아야 함.
        // 여기서는 편의상 DTO에 regip 필드를 추가하는게 좋음. 일단 없이 진행하면 null 들어감.

        int cnt = 0;
        try {
            cnt = instructorMemberMapper.insertInstructor(iDomain);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return cnt > 0;
    }

    public String chkId(String id) {
        String data = null;
        try {
            data = instructorMemberMapper.selectId(id);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return data;
    }

}
