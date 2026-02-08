package kr.co.sist.instructor.member;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InstructorDomain {
    private String instId, password, name, email, phone, profile, regip;
    private Date birth, regDate;
    private int activation, approval;
}
