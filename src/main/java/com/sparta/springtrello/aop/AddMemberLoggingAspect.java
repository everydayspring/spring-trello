package com.sparta.springtrello.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sparta.springtrello.config.SlackNotifier;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AddMemberLoggingAspect {

    @Autowired private SlackNotifier slackNotifier;

    // addMember 메서드에 대한 포인트컷 설정
    @Pointcut(
            "execution(* com.sparta.springtrello.domain.workspace.service.WorkspaceService.addMember(..))")
    public void addMemberPointcut() {}

    // addMember 메서드가 성공적으로 반환된 후 실행
    @AfterReturning(value = "addMemberPointcut()", returning = "result")
    public void logAddMemberSuccess(org.aspectj.lang.JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();

        // addMember의 인수인 AuthUser와 request 정보를 가져옴
        Object authUser = args[0]; // AuthUser 객체
        Object workspaceId = args[1]; // workspaceId
        Object email = args[2]; // 추가할 멤버의 email
        Object role = args[3]; // WorkspaceUserRole

        // 알림 메시지 생성
        String message =
                String.format(
                        "addMember 성공: authUser=%s, workspaceId=%s, email=%s, role=%s",
                        authUser, workspaceId, email, role);

        // Slack 알림 전송
        slackNotifier.sendSlackNotification(message);

        // 로그 출력
        log.info(message);
    }
}
