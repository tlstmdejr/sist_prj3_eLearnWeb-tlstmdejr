package kr.co.sist.dao;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisHandler {
	private static MyBatisHandler mbh;
	private static SqlSessionFactory ssf;
	private MyBatisHandler() {
		
	}
	public static  MyBatisHandler getInstance() {
		if(mbh==null) {
			mbh=new MyBatisHandler();
		}
		return mbh;
	}
	public static SqlSessionFactory getSessFactory()throws IOException {
		if(ssf==null) {
			Reader reader=Resources.getResourceAsReader("kr/co/sist/dao/mybatis-config.xml");
			ssf=new SqlSessionFactoryBuilder().build(reader);		
			System.out.println(ssf);
			if(reader!=null) {
				reader.close();
			}

		}
		return ssf;
	}
	
	public SqlSession getMyBatisHandler(boolean autoCommitFlag) {
		SqlSession ss=null;
		try {
		ss=getSessFactory().openSession(autoCommitFlag);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return ss;
	}
}
//class
