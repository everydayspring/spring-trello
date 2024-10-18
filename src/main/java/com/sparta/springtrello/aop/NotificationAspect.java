package com.sparta.springtrello.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sparta.springtrello.config.SlackNotifier;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.comment.dto.request.CommentRequest;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class NotificationAspect {

    @Autowired private UserRepository userRepository;

    @Autowired private SlackNotifier slackNotifier;

    @Pointcut(
            "execution(* com.sparta.springtrello.domain.workspace.service.WorkspaceService.addMember(..))")
    public void addMemberPointcut() {}

    @Pointcut("execution(* com.sparta.springtrello.domain.card.service.CardService.createCard(..))")
    public void createCardPointcut() {}

    @Pointcut("execution(* com.sparta.springtrello.domain.card.service.CardService.updateCard(..))")
    public void updateCardPointcut() {}

    @Pointcut(
            "execution(* com.sparta.springtrello.domain.comment.service.CommentService.saveComment(..))")
    public void saveCommentPointcut() {}

    @AfterReturning(value = "addMemberPointcut()", returning = "result")
    public void logAddMemberSuccess(org.aspectj.lang.JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();

        AuthUser authUser = (AuthUser) args[0];
        Object workspaceId = args[1];
        Object role = args[3];

        String title = "Workspace Add User Event";

        User user = (User) result;
        String userTag = "<@" + user.getSlackId() + ">";

        String authTag = "<@" + authUser.getSlackId() + ">";

        String message =
                String.format(
                        "- Workspace ID : %s \n- Role : %s \n- 추가한 사용자 : %s \n- 추가된 사용자 : %s",
                        workspaceId, role, authTag, userTag);

        sendNotification(title, message);
    }

    @AfterReturning(value = "createCardPointcut()", returning = "result")
    public void logCreateCardSuccess(Object result) {

        Card card = (Card) result;

        User user = getUser(card.getManagerId());
        String userTag = "<@" + user.getSlackId() + ">";

        String title = "New Card Created Event";
        String message =
                String.format(
                        "- Card Name : %s \n- Card Description : %s \n- List ID : %s \n- DueDate : %s \n- 담당자 : %s",
                        card.getName(),
                        card.getDescription(),
                        card.getListId(),
                        card.getDueDate(),
                        userTag);

        sendNotification(title, message);
    }

    @AfterReturning(value = "updateCardPointcut()", returning = "result")
    public void logCardUpdateSuccess(Object result) {
        Card card = (Card) result;

        User user = getUser(card.getManagerId());
        String userTag = "<@" + user.getSlackId() + ">";

        String title = "Card Updated Event";
        String message =
                String.format(
                        "- Card Name : %s \n- Card Description : %s \n- List ID : %s \n- DueDate : %s \n- 담당자 : %s",
                        card.getName(),
                        card.getDescription(),
                        card.getListId(),
                        card.getDueDate(),
                        userTag);

        sendNotification(title, message);
    }

    @AfterReturning(value = "saveCommentPointcut()")
    public void logCommentSuccess(org.aspectj.lang.JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        AuthUser authUser = (AuthUser) args[0];
        Object commentRequest = args[1];

        String authTag = "<@" + authUser.getSlackId() + ">";

        String title = "New Comment Event";
        String message =
                String.format(
                        "- Card ID : %s \n- Emoji : %s \n- Content : %s \n- 작성자 : %s",
                        ((CommentRequest) commentRequest).getCardId(),
                        ((CommentRequest) commentRequest).getEmoji(),
                        ((CommentRequest) commentRequest).getContent(),
                        authTag);

        sendNotification(title, message);
    }

    private void sendNotification(String title, String message) {
        try {
            slackNotifier.sendSlackNotification(title, message);
            log.info("Slack notification sent: {}", message);
        } catch (Exception e) {
            log.error("Failed to send Slack notification: {}", e.getMessage());
        }
    }

    private User getUser(Long managerId) {
        User user = userRepository.findById(managerId).orElse(null);

        return user;
    }
}
