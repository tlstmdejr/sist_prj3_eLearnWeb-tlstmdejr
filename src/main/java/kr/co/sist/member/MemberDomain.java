package kr.co.sist.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDomain {
	private String id;
	private String name;
	private String password;
	private String email;
	private String resultMsg;
}
