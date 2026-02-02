package kr.co.sist.user.member;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {

    private String id, password, email, intro, name, img, phone, regip;
    private Date birth, regdate;
    private char activation;
    

}
