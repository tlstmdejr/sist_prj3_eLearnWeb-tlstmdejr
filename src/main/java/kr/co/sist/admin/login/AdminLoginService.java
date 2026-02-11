package kr.co.sist.admin.login;

import kr.co.sist.admin.member.AdminDTO;
import kr.co.sist.admin.member.AdminDomain;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

/**
 * 관리자 - 로그인 서비스
 */
@Service
public class AdminLoginService {

    private final AdminLoginMapper adminLoginMapper;

    public AdminLoginService(AdminLoginMapper adminLoginMapper) {
        this.adminLoginMapper = adminLoginMapper;
    }

    /**
     * 로그인 인증
     * 
     * @param adminDTO 로그인 정보
     * @return 로그인 성공 시 관리자 도메인, 실패 시 null
     */
    public AdminDomain login(AdminDTO adminDTO) {
        AdminDomain adminDomain = null;
        try {
            AdminDomain tempAdmin = adminLoginMapper.selectAdmin(adminDTO.getId());
            if (tempAdmin != null && tempAdmin.getPassword().equals(adminDTO.getPassword())) {
                adminDomain = tempAdmin;
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return adminDomain;
    }

}
