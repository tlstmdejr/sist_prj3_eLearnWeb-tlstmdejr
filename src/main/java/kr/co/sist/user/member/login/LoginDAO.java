package kr.co.sist.user.member.login;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.sist.dao.MyBatisHandler;
import kr.co.sist.user.member.StudentDTO;
import kr.co.sist.user.member.StudentDomain;

@Repository
public class LoginDAO {

	public StudentDomain selectOneStu(String userId)throws PersistenceException{
		StudentDomain sd=null;
		
		SqlSession ss=MyBatisHandler.getInstance().getMyBatisHandler(false);
		sd=ss.selectOne("kr.co.sist.user.member.login.selectOneUserInfo",userId);
		if( ss != null) { ss.close(); }//end if
		
		return sd;
	}

}
//class
