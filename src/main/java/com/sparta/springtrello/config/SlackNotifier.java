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

    public void sendSlackNotification(String title, String message) {
        Map<String, Object> payload = new HashMap<>();

        String formattedMessage = "*" + title + "*\n" + message;

        payload.put("text", formattedMessage);
        payload.put("username", "Spring Trello Event");
        payload.put("icon_emoji", ":smiley:");

        try {
            restTemplate.postForEntity(slackWebhookUrl, payload, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
