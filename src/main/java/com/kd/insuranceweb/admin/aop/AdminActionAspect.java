package com.kd.insuranceweb.admin.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import com.kd.insuranceweb.admin.annotation.AdminActionLog;
import com.kd.insuranceweb.admin.service.AdminActivityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class AdminActionAspect {

    private final AdminActivityService activityService;
    
    public AdminActionAspect(AdminActivityService activityService) {
        this.activityService = activityService;
        log.info("✅ AdminActionAspect Bean 등록 완료");
    }

    // @AdminActionLog 붙은 메서드가 정상 리턴하면 실행
    @AfterReturning(value = "@annotation(actionLog)", argNames = "joinPoint,actionLog")
    public void afterAction(JoinPoint joinPoint, AdminActionLog actionLog) {
        try {
            // TODO: 실제 로그인 연동 전까지 임시값
            Integer employee_id = 1;
            String employee_name = "관리자";

            log.info("✅ AOP 실행됨: " + actionLog.value());
            
            activityService.recordActivity(
                employee_id,
                employee_name,
                actionLog.type(),
                actionLog.value()
            );
        } catch (Exception e) {
            log.error("관리자 활동 로그 기록 실패", e);
        }
    }

}
