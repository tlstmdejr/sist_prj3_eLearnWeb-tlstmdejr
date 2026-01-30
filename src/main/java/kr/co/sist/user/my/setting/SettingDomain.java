package kr.co.sist.user.my.setting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 설정 페이지 정보를 담는 Domain 클래스
 * - 프로필 사진 & 닉네임 & 아이디 & 자기소개
 * - 이메일, 비밀번호(마스킹), 휴대폰 번호
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SettingDomain {
    
    private String id;        // 아이디
    private String name;      // 닉네임
    private String img;       // 프로필 이미지 경로
    private String intro;     // 자기소개
    private String email;     // 이메일
    private String phone;     // 휴대폰 번호
    private String password;  // 비밀번호 (마스킹 처리용)
}
// class
