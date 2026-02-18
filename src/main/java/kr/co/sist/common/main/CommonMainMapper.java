package kr.co.sist.common.main;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

@Mapper
public interface CommonMainMapper {

    public List<CommonMainDomain> selectCategoryList() throws PersistenceException;

    public List<CommonMainDomain> selectCourseList() throws PersistenceException;

}
