package kr.co.sist.common.member;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;
import kr.co.sist.common.member.CommonMemberMapper;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

/**
 * 공통 - 회원 멤버 서비스 (아이디/비번 찾기)
 */
@Service
public class CommonMemberService {

    private final CommonMemberMapper commonMemberMapper;
    private final net.nurigo.sdk.message.service.DefaultMessageService messageService;

    @org.springframework.beans.factory.annotation.Value("${solapi.sender.number}")
    private String senderNumber;

    public CommonMemberService(CommonMemberMapper commonMemberMapper,
            @org.springframework.beans.factory.annotation.Value("${solapi.api.key}") String apiKey,
            @org.springframework.beans.factory.annotation.Value("${solapi.api.secret}") String apiSecret) {
        this.commonMemberMapper = commonMemberMapper;
        this.messageService = net.nurigo.sdk.NurigoApp.initialize(apiKey, apiSecret, "https://api.solapi.com");
    }

    /**
     * 인증번호 발송
     * 
     * @param phone 수신 번호
     * @return 발송된 인증번호 (실패 시 null)
     */
    public String sendAuthCode(String phone) {
        // 6자리 랜덤 인증번호 생성
        String authCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        String text = "[IntLearn] 인증번호는 [" + authCode + "] 입니다.";

        if (sendSms(phone, text)) {
            return authCode;
        }
        return null;
    }

    /**
     * 아이디 찾기 (SMS 전송)
     * 
     * @param phone 수신 번호
     * @return 처리 결과 메시지
     */
    public String findId(String phone) {
        String userId = null;
        try {
            userId = commonMemberMapper.selectUserByPhone(phone);
            if (userId == null) {
                userId = commonMemberMapper.selectInstructorByPhone(phone);
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }

        if (userId != null) {
            String text = "[IntLearn] 회원님의 아이디는 [" + userId + "] 입니다.";
            if (sendSms(phone, text)) {
                return "SMS로 아이디가 발송되었습니다.";
            } else {
                return "SMS 발송에 실패했습니다.";
            }
        }
        return "일치하는 회원 정보가 없습니다.";
    }

    /**
     * 비밀번호 재설정
     * 
     * @param id    아이디
     * @param newPw 새 비밀번호
     * @return 성공 여부
     */
    public boolean updatePassword(String id, String newPw) {
        // 비밀번호 암호화
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String encodedPw = encoder.encode(newPw);

        java.util.Map<String, String> params = new java.util.HashMap<>();
        params.put("id", id);
        params.put("password", encodedPw);

        int cnt = 0;
        try {
            // 사용자/강사 모두 시도 (아이디가 PK이므로 겹치지 않는다고 가정하거나, 구분 필요)
            // 여기서는 둘 다 시도 updateUserPassword, updateInstructorPassword
            // 보통 아이디 중복이 안되므로.
            cnt = commonMemberMapper.updateUserPassword(params);
            if (cnt == 0) {
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
