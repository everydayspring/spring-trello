package com.sparta.springtrello.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SlackNotifier {

    @Value("${slack.webhook.url}")
    private String slackWebhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 제목과 메시지를 함께 전송하도록 메서드 수정
    public void sendSlackNotification(String title, String message) {
        Map<String, Object> payload = new HashMap<>();
        // 제목을 메시지의 첫 부분에 추가
        String formattedMessage = "*" + title + "*\n" + message; // 제목은 굵은 글씨로

        payload.put("text", formattedMessage); // 메시지 내용에 제목 포함

        try {
            restTemplate.postForEntity(slackWebhookUrl, payload, String.class);
        } catch (Exception e) {
            e.printStackTrace(); // 에러 처리
        }
    }
}
