package com.kd.insuranceweb.admin.mapper;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.admin.dto.AuthLogDto;
import com.kd.insuranceweb.admin.dto.RoleDto;

@Mapper
public interface EmployeeRoleMapper {
    List<RoleDto> findRolesByEmployee(@Param("emp_id") Integer emp_id);
    int grantRoleToEmployee(@Param("emp_id") Integer emp_id, @Param("role_id") Integer role_id);
    int revokeRoleFromEmployee(@Param("emp_id") Integer emp_id, @Param("role_id") Integer role_id);

    int insertAuthLog(@Param("action_type") String actionType,
                      @Param("target_emp_id") Integer targetEmpId,
                      @Param("role_id") Integer roleId,
                      @Param("changed_by_id") Integer changedById,
                      @Param("remarks") String remarks);
    
    List<AuthLogDto> selectAuthLogs(
            @Param("target_emp_name") String targetEmpName,
            @Param("start_date") Date startDate,
            @Param("end_date") Date endDate
        );
}