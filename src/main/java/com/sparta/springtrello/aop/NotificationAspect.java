package com.sparta.springtrello.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sparta.springtrello.config.SlackNotifier;
import com.sparta.springtrello.domain.comment.dto.request.CommentRequest;
import com.sparta.springtrello.domain.common.dto.AuthUser;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class NotificationAspect {

    @Autowired private SlackNotifier slackNotifier;

    // addMember 메서드에 대한 포인트컷 설정
    @Pointcut(
            "execution(* com.sparta.springtrello.domain.workspace.service.WorkspaceService.addMember(..))")
    public void addMemberPointcut() {}

    // 댓글 작성 메서드에 대한 포인트컷 설정
    @Pointcut(
            "execution(* com.sparta.springtrello.domain.comment.service.CommentService.saveComment(..))")
    public void saveCommentPointcut() {}

    @AfterReturning(value = "addMemberPointcut()", returning = "result")
    public void logAddMemberSuccess(org.aspectj.lang.JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();

        // AuthUser 객체를 Object에서 AuthUser로 캐스팅
        AuthUser authUser = (AuthUser) args[0]; // AuthUser 객체
        Object workspaceId = args[1]; // workspaceId
        Object email = args[2]; // 추가할 멤버의 email
        Object role = args[3]; // WorkspaceUserRole

        String title = "Workspace Add User Event";
        // 알림 메시지 생성
        String message =
                String.format(
                        "- AuthUser = %s \n - Slack ID = %s \n - Workspace ID = %s \n - Add User Email = %s \n - Role = %s \n - slack = <@%s>",
                        authUser.getEmail(),
                        authUser.getSlackId(),
                        workspaceId,
                        email,
                        role,
                        authUser.getSlackId());

        // Slack 알림 전송
        sendNotification(title, message);
    }

    // saveComment 메서드가 성공적으로 반환된 후 실행
    @AfterReturning(value = "saveCommentPointcut()", returning = "result")
    public void logCommentSuccess(org.aspectj.lang.JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();

        // saveComment의 인수인 AuthUser와 관련 정보를 가져옴
        Object authUser = args[0]; // AuthUser 객체
        Object commentRequest = args[1]; // CommentRequest 객체

        // 알림 제목과 메시지
        String title = "New Comment Event";
        String message =
                String.format(
                        " - AuthUser = %s \n - Card ID = %s \n - Emoji = %s \n - Content = %s",
                        authUser,
                        ((CommentRequest) commentRequest).getCardId(),
                        ((CommentRequest) commentRequest).getEmoji(),
                        ((CommentRequest) commentRequest).getContent());

        // Slack 알림 전송
        sendNotification(title, message);
    }

    // Slack 알림 전송을 위한 공통 메서드
    private void sendNotification(String title, String message) {
        try {
            slackNotifier.sendSlackNotification(title, message);
            log.info("Slack notification sent: {}", message);
        } catch (Exception e) {
            log.error("Failed to send Slack notification: {}", e.getMessage());
        }
    }
}
