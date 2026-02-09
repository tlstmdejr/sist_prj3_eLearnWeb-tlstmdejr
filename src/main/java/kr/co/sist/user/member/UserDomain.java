package kr.co.sist.user.member;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDomain {

    private String id, password, email, intro, name, img, phone, regIp;
    private Date birth, regDate;
    private int activation;

}
