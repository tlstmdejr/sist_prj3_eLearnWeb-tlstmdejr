package kr.co.sist.user.member;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StudentDTO {

    private String id,password,email,intro,name,img,phone;
    private Date birth;
    private char activation;
    

}
