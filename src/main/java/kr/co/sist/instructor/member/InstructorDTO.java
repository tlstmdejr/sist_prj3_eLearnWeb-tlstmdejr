package kr.co.sist.instructor.member;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InstructorDTO {
    private String instId, password, name, phone, email, profile, oath, regip;
    private Date birth;
}
