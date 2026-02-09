package kr.co.sist.common.email;

import java.util.Properties;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    private static final String ENCODING = "UTF-8";

    /**
     * 이메일 인증 코드 발송
     * 
     * @param to 수신자 이메일
     * @return 생성된 인증 코드 (실패 시 null)
     */
    public String sendAuthCode(String to) {
        // 공백 제거 (설정 파일 오류 방지)
        if (password != null) {
            password = password.replace(" ", "");
        }

        String authCode = createAuthCode();
        String subject = "[2GV] 이메일 인증 번호입니다.";
        String content = "<div style='text-align:center; border:1px solid #ddd; padding:20px; font-family:sans-serif;'>"
                + "<h2>이메일 인증 번호</h2>"
                + "<p>아래 인증 번호를 입력하여 이메일 인증을 완료해주세요.</p>"
                + "<div style='background:#f5f5f5; padding:15px; font-size:24px; font-weight:bold; letter-spacing:5px; margin:20px 0;'>"
                + authCode
                + "</div>"
                + "<p style='color:#888; font-size:12px;'>본 메일은 발신 전용입니다.</p>"
                + "</div>";

        boolean isSent = sendEmail(to, subject, content);
        return isSent ? authCode : null;
    }

    /**
     * 이메일 전송 (javax.mail 직접 사용 - sist_prj2_movieWeb 참조)
     */
    public boolean sendEmail(String to, String subject, String content) {
        Session session = getSession();

        if (session == null) {
            System.out.println("메일 전송 실패: Session 생성 실패");
            return false;
        }

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail, "2GV 관리자", ENCODING));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setContent(content, "text/html; charset=" + ENCODING);

            Transport.send(msg);
            System.out.println("메일 발송 성공: " + to);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("메일 발송 실패: " + e.getMessage());
            return false;
        }
    }

    /**
     * SMTP Session 설정
     */
    private Session getSession() {
        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "false"); // TLS 사용
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            return Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 인증 코드 생성 (영문+숫자 6자리)
     */
    private String createAuthCode() {
        int leftLimit = 48; // '0'
        int rightLimit = 122; // 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
