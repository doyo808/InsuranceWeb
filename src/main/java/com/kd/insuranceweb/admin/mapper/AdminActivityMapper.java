package com.kd.insuranceweb.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kd.insuranceweb.admin.dto.AdminActivityLogDTO;

@Mapper
public interface AdminActivityMapper {

    /**
     * 관리자 활동 로그 추가
     */
    void insertActivity(AdminActivityLogDTO log);

    /**
     * 최근 관리자 활동 10개 조회
     */
    List<AdminActivityLogDTO> getRecentActivities();
}
