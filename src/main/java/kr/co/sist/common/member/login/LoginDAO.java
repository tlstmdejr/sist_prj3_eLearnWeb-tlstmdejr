package kr.co.sist.member.login;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.sist.dao.MyBatisHandler;
import kr.co.sist.member.MemberDomain;

@Repository
public class LoginDAO {

	public MemberDomain selectOneMember(LoginDTO lDTO)throws PersistenceException{
		MemberDomain md=null;
		
		SqlSession ss=MyBatisHandler.getInstance().getMyBatisHandler(false);
		md=ss.selectOne("kr.co.sist.member.login.selectOneUserInfo",lDTO);
		if( ss != null) { ss.close(); }//end if
		
		return md;
	}
	
	public void insertLoginHistory(LoginDTO lDTO)throws PersistenceException{
		
		
		SqlSession ss=MyBatisHandler.getInstance().getMyBatisHandler(true);
		ss.insert("kr.co.sist.member.login.loginHistory",lDTO);
		
		if( ss != null) { ss.close(); }//end if
	}
}
//class
