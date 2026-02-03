package kr.co.sist.user.member;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;

/**
 * 학생 회원 관련 비즈니스 로직을 처리하는 Service 클래스
 * - 회원가입, 정보조회/수정, 아이디/비밀번호 찾기, 탈퇴 등
 */
@Service
public class UserService {

    @Value("${user.crypto.key}")
    private String key;
    @Value("${user.crypto.salt}")
    private String salt;

    @Autowired
    private UserMapper um;

    // /** 로그인서비스로이전(id확인 비밀번호확인,암호화까지)
    // * 학생 로그인 처리
    // * @param userId 사용자 아이디
    // * @param userPass 사용자 비밀번호
    // * @return UserSessionDTO 로그인 성공 시 세션에 저장할 사용자 정보
    // */
    // public UserSessionDTO loginUser(String userId, String userPass) {
    // UserSessionDTO usDTO = null;

    // // TODO: 로그인 로직 구현
    // // 1. sDAO.selectLogin() 호출하여 사용자 정보 조회
    // // 2. 비밀번호 일치 확인 (BCrypt 등)
    // // 3. 로그인 성공 시 UserSessionDTO 생성 및 반환

    // return usDTO;
    // }

    /**
     * 학생 회원가입 처리
     * 
     * @param sDTO 회원가입 정보가 담긴 DTO
     * @return int 회원가입 결과 (1: 성공, 0: 실패)
     */
    public boolean addUser(UserDTO sDTO) {

        // TODO: 회원가입 로직 구현
        // 1. 비밀번호 암호화 (BCrypt)
        // 2. 개인정보 암호화 (이름, 이메일 등)
        // 3. sDAO.insertStu() 호출
        boolean flag = false;
        // 일방향 해시 : 비번
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder(10);
        sDTO.setPassword(bpe.encode(sDTO.getPassword()));

        // 암호화
        TextEncryptor te = createEncryptor();
        sDTO.setName(te.encrypt(sDTO.getName()));
        String email = sDTO.getEmail();
        if (email != null && !email.isEmpty()) {
            sDTO.setEmail(te.encrypt(email));
        }
        try {
            um.insertUser(sDTO);
            flag = true;
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return flag;
    }

    /**
     * 학생 아이디 중복확인 (AJAX)
     * 
     * @param stuId 확인할 아이디
     * @return boolean 중복 여부 (true: 사용가능, false: 중복)
     */
    public boolean chkUserId(String stuId) {
        boolean isAvailable = false;
        try {
            String id = um.selectId(stuId);
            if (id == null) {
                isAvailable = true;
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return isAvailable;
    }

    /**
     * 학생 이름 중복확인 (AJAX)
     * 
     * @param name 확인할 이름
     * @return boolean 중복 여부 (true: 사용가능, false: 중복)
     */
    public boolean chkUserName(String name) {
        boolean isAvailable = false;
        try {
            // 이름은 암호화되어 저장되므로 암호화 후 조회
            TextEncryptor te = createEncryptor();
            String encName = te.encrypt(name);

            String result = um.selectName(encName);
            if (result == null) {
                isAvailable = true;
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return isAvailable;
    }

    /**
     * 학생 정보 상세조회 (마이페이지)
     * - 학생/강사 DB구조가 다르므로 따로 구현
     * 
     * @param stuId 조회할 학생 아이디
     * @return StudentDomain 학생 상세정보
     */
    public UserDomain getUserDetail(String stuId) {
        UserDomain sd = null;

        // TODO: 학생 정보 조회 로직 구현
        // 1. sDAO.selectOneStu() 호출
        // 2. 암호화된 개인정보 복호화

        return sd;
    }

    /**
     * 학생 정보 수정
     * 
     * @param sDTO 수정할 학생 정보가 담긴 DTO
     * @return int 수정 결과 (1: 성공, 0: 실패)
     */
    public int modifyUserInfo(UserDTO sDTO) {
        int result = 0;

        // TODO: 정보 수정 로직 구현
        // 1. 수정할 정보 암호화 (필요시)
        // 2. sDAO.updateStu() 호출

        return result;
    }
    // .common으로 이동시켜서 공통으로 사용할예정
    // /**
    // * 학생 아이디 찾기
    // * @param userPhone 사용자 전화번호
    // * @return String 찾은 아이디 (없으면 null)
    // */
    // public String findUserId(String userPhone) {
    // String userId = null;

    // // TODO: 아이디 찾기 로직 구현
    // // sDAO.selectId() 호출

    // return userId;
    // }

    // /**
    // * 학생 비밀번호 찾기
    // * @param userId 사용자 아이디
    // * @param userPhone 사용자 전화번호
    // * @param userEmail 사용자 이메일
    // * @return String 찾은 비밀번호 (임시 비밀번호 발급 또는 null)
    // */
    // public String findUserPass(String userId, String userPhone, String userEmail)
    // {
    // String userPass = null;

    // // TODO: 비밀번호 찾기 로직 구현
    // // 1. sDAO.selectPass() 호출하여 사용자 확인
    // // 2. 임시 비밀번호 생성 및 이메일/SMS 발송
    // // 3. DB에 임시 비밀번호 업데이트

    // return userPass;

    // } .common으로 이동시켜서 공통으로 사용할예정

    /**
     * 학생 회원 탈퇴
     * 
     * @param sDTO 탈퇴할 학생 정보 (아이디, 비밀번호)
     * @return boolean 탈퇴 성공 여부
     */
    public boolean withdrawUser(UserDTO sDTO) {
        boolean isWithdrawn = false;

        // TODO: 회원 탈퇴 로직 구현
        // 1. 비밀번호 확인
        // 2. sDAO.deleteStu() 호출 (실제 삭제 또는 activation 변경)

        return isWithdrawn;
    }

    /**
     * 결정적 암호화를 위한 Encryptor 생성 (검색 가능하도록 고정 IV 사용)
     */
    private TextEncryptor createEncryptor() {
        return new TextEncryptor() {
            // 고정 IV 사용 (0으로 초기화된 16바이트)
            private final AesBytesEncryptor encryptor = new AesBytesEncryptor(key, salt, new BytesKeyGenerator() {
                @Override
                public int getKeyLength() {
                    return 16;
                }

                @Override
                public byte[] generateKey() {
                    return new byte[16];
                }
            });

            @Override
            public String encrypt(String text) {
                return new String(Hex.encode(encryptor.encrypt(text.getBytes(StandardCharsets.UTF_8))));
            }

            @Override
            public String decrypt(String encryptedText) {
                return new String(encryptor.decrypt(Hex.decode(encryptedText)), StandardCharsets.UTF_8);
            }
        };
    }

}
// class
