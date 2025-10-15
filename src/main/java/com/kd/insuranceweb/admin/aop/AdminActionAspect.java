package com.kd.insuranceweb.admin.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import com.kd.insuranceweb.admin.annotation.AdminActionLog;
import com.kd.insuranceweb.admin.service.AdminActivityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminActionAspect {

    private final AdminActivityService activityService;

    @AfterReturning("@annotation(actionLog)")
    public void afterAction(JoinPoint joinPoint, AdminActionLog actionLog) {
        try {
            // 현재 로그인 관리자 정보 (Spring Security 세션에서)
            String employeeName = "관리자"; // SecurityContextHolder에서 가져오도록 교체 가능
            Long employeeId = 1L;

            activityService.recordActivity(
                employeeId,
                employeeName,
                actionLog.type(),
                actionLog.value(),
                extractTargetId(joinPoint.getArgs())
            );
        } catch (Exception e) {
            log.error("관리자 활동 로그 기록 실패", e);
        }
    }

    private String extractTargetId(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Number) {
                return arg.toString();
            }
        }
        return null;
    }
}
