package kr.co.sist.common.member;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

/**
 * 공통 - 회원 멤버 서비스 (아이디/비번 찾기)
 */
@Service
public class CommonMemberService {

    private final CommonMemberMapper commonMemberMapper;
    private final net.nurigo.sdk.message.service.DefaultMessageService messageService;
    private final kr.co.sist.common.email.EmailService emailService;
    private final kr.co.sist.common.util.CryptoUtil cryptoUtil;

    @org.springframework.beans.factory.annotation.Value("${solapi.sender.number}")
    private String senderNumber;

    public CommonMemberService(CommonMemberMapper commonMemberMapper,
            kr.co.sist.common.email.EmailService emailService,
            kr.co.sist.common.util.CryptoUtil cryptoUtil,
            @org.springframework.beans.factory.annotation.Value("${solapi.api.key}") String apiKey,
            @org.springframework.beans.factory.annotation.Value("${solapi.api.secret}") String apiSecret) {
        this.commonMemberMapper = commonMemberMapper;
        this.emailService = emailService;
        this.cryptoUtil = cryptoUtil;
        this.messageService = net.nurigo.sdk.NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.solapi.com");
    }

    /**
     * 인증번호 발송
     * 
     * @param phone 수신 번호
     * @return 발송된 인증번호 (실패 시 null)
     */
    /**
     * 아이디 찾기용 인증번호 발송
     * 
     * @param phone 수신 번호
     * @return 발송된 인증번호 (회원 없으면 "not_found", 실패 시 null)
     */
    public String sendIdAuthCode(String phone) {
        // 1. 회원 존재 여부 확인
        String userId = null;
        try {
            userId = commonMemberMapper.selectUserByPhone(phone);
            if (userId == null) {
                userId = commonMemberMapper.selectInstructorByPhone(phone);
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        if (userId == null) {
            return "not_found";
        }

        // 2. 인증번호 생성 및 발송
        String authCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        String text = "[IntLearn] 아이디 찾기 인증번호는 [" + authCode + "] 입니다.";

        if (sendSms(phone, text)) {
            return authCode;
        }
        return null;
    }

    /**
     * 휴대폰 번호 인증 (단순 발송 - 정보수정 등)
     * 
     * @param phone 수신 번호
     * @return 발송된 인증번호
     */
    public String sendPhoneVerificationCode(String phone) {
        // 6자리 랜덤 인증번호 생성
        String authCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        String text = "[IntLearn] 인증번호는 [" + authCode + "] 입니다.";

        if (sendSms(phone, text)) {
            return authCode;
        }
        return null;
    }

    /**
     * 아이디 찾기 (인증 후 ID 반환)
     * 
     * @param phone 수신 번호
     * @return 회원 아이디
     */
    public String findIdByPhone(String phone) {
        String userId = null;
        try {
            userId = commonMemberMapper.selectUserByPhone(phone);
            if (userId == null) {
                userId = commonMemberMapper.selectInstructorByPhone(phone);
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return userId;
    }

    /**
     * 비밀번호 찾기 (정보 확인 및 이메일 전송)
     * 
     * @param type 회원 유형 (user/instructor)
     * @param id   아이디
     * @param name 이름
     * @return 마스킹된 이메일 / not_found / null
     */
    public String sendPwAuthCode(String type, String id, String name) {
        String foundEmail = null;

        try {
            // 이름 암호화 (DB에 암호화됐 저장됨)
            String encryptedName = cryptoUtil.encrypt(name);

            java.util.Map<String, String> params = new java.util.HashMap<>();
            params.put("id", id);
            params.put("name", encryptedName);

            if ("user".equals(type)) {
                foundEmail = commonMemberMapper.selectUserEmailByInfo(params);
            } else if ("instructor".equals(type)) {
                foundEmail = commonMemberMapper.selectInstructorEmailByInfo(params);
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        if (foundEmail == null) {
            return "not_found";
        }

        // 이메일 복호화
        String decryptedEmail = null;
        try {
            decryptedEmail = cryptoUtil.decrypt(foundEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 복호화 실패 시 중단
        }

        // 이메일로 인증번호 발송
        String code = emailService.sendAuthCode(decryptedEmail);
        if (code != null) {
            // 마스킹된 이메일 반환 (예: t***@gmail.com)
            return maskEmail(decryptedEmail) + "##" + code;
        }
        return null;
    }

    /**
     * 이메일 마스킹 (t***@gmail.com)
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@"))
            return email;
        int atIndex = email.indexOf("@");
        if (atIndex <= 1)
            return email;
        return email.charAt(0) + "***" + email.substring(atIndex);
    }

    /**
     * 비밀번호 재설정
     * 
     * @param type  회원 유형
     * @param id    아이디
     * @param newPw 새 비밀번호
     * @return 성공 여부
     */
    public boolean updatePassword(String type, String id, String newPw) {
        // 비밀번호 암호화
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

        // 1. 현재 비밀번호 확인 (재사용 방지)
        String currentHash = null;
        try {
            if ("user".equals(type)) {
                currentHash = commonMemberMapper.selectUserPassword(id);
            } else if ("instructor".equals(type)) {
                currentHash = commonMemberMapper.selectInstructorPassword(id);
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            return false;
        }

        // 2. 기존 비밀번호와 동일한지 검사
        if (currentHash != null && encoder.matches(newPw, currentHash)) {
            return false; // 기존 비밀번호와 동일하면 변경 불가
        }

        String encodedPw = encoder.encode(newPw);

        java.util.Map<String, String> params = new java.util.HashMap<>();
        params.put("id", id);
        params.put("password", encodedPw);

        int cnt = 0;
        try {
            if ("user".equals(type)) {
                cnt = commonMemberMapper.updateUserPassword(params);
            } else if ("instructor".equals(type)) {
                cnt = commonMemberMapper.updateInstructorPassword(params);
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return cnt > 0;
    }

    /**
     * SMS 발송 내부 메서드
     * 
     * @param phone 수신 번호
     * @param text  메시지 내용
     * @return 발송 성공 여부
     */
    private boolean sendSms(String phone, String text) {
        net.nurigo.sdk.message.model.Message message = new net.nurigo.sdk.message.model.Message();
        message.setFrom(senderNumber);
        message.setTo(phone);
        message.setText(text);

        try {
            messageService.sendOne(new net.nurigo.sdk.message.request.SingleMessageSendingRequest(message));

            // DB 로그 저장
            java.util.Map<String, String> params = new java.util.HashMap<>();
            params.put("phone", phone);
            params.put("email", ""); // 이메일은 정보 없음
            commonMemberMapper.insertTransmissionHistory(params);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
