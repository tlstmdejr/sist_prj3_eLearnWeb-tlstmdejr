package kr.co.sist.common.main;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonMainService {

    @Autowired
    private CommonMainMapper cmm;

    public List<CommonMainDomain> getCategoryList() {
        List<CommonMainDomain> list = null;
        try {
            list = cmm.selectCategoryList();
        } catch (PersistenceException pe) {
            log.error("카테고리 목록 조회 실패", pe);
        }
        return list;
    }

    public List<CommonMainDomain> getCourseList() {
        List<CommonMainDomain> list = null;
        try {
            list = cmm.selectCourseList();
        } catch (PersistenceException pe) {
            log.error("강의 목록 조회 실패", pe);
        }
        return list;
    }

}
