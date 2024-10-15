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

    public void sendSlackNotification(String message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("text", message); // 메시지 내용

        try {
            restTemplate.postForEntity(slackWebhookUrl, payload, String.class);
        } catch (Exception e) {
            e.printStackTrace(); // 에러 처리
        }
    }
}
