package kr.co.sist.instructor.member;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.sist.common.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 강사 - 회원가입(Member) 서비스
 */
@Slf4j
@Service
public class InstructorService {

    private final InstructorMemberMapper instructorMemberMapper;

    @Autowired
    private CryptoUtil cryptoUtil; // 공통 암호화 유틸 주입

    public InstructorService(InstructorMemberMapper instructorMemberMapper) {
        this.instructorMemberMapper = instructorMemberMapper;
    }

    public boolean addInstructor(InstructorDTO iDTO) {
        boolean flag = false;

        // 비밀번호 단방향 암호화 (BCrypt)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        iDTO.setPassword(encoder.encode(iDTO.getPassword()));

        // 개인정보 양방향 암호화 (AES) - 이름, 이메일
        iDTO.setName(cryptoUtil.encrypt(iDTO.getName()));
        String email = iDTO.getEmail();
        if (email != null && !email.isEmpty()) {
            iDTO.setEmail(cryptoUtil.encrypt(email));
        }

        try {
            instructorMemberMapper.insertInstructor(iDTO);
            flag = true;
        } catch (PersistenceException pe) {
            log.error("강사 등록 실패 - instId: {}", iDTO.getInstId(), pe);
        }
        return flag;
    }

    public String chkId(String id) {
        String data = null;
        try {
            data = instructorMemberMapper.selectId(id);
        } catch (PersistenceException pe) {
            log.error("강사 아이디 중복확인 실패 - id: {}", id, pe);
        }
        // DB에 없으면(null) 사용 가능, 있으면 중복
        return (data == null) ? "available" : "duplicate";
    }

    /**
     * 이름 중복 확인
     */
    public String chkName(String name) {
        String data = null;
        try {
            data = instructorMemberMapper.selectName(name);
        } catch (PersistenceException pe) {
            log.error("강사 이름 중복확인 실패", pe);
        }
        return (data == null) ? "available" : "duplicate";
    }

    /**
     * 전화번호 중복 확인
     */
    public String chkPhone(String phone) {
        String data = null;
        try {
            data = instructorMemberMapper.selectPhone(phone);
        } catch (PersistenceException pe) {
            log.error("강사 전화번호 중복확인 실패", pe);
        }
        return (data == null) ? "available" : "duplicate";
    }

}
