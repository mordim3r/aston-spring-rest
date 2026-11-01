package com.example.astonspringrest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test-notifications")
public class NotificationController {

    private final NotificationClient notificationClient;

    public NotificationController(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @PostMapping
    public String send(@RequestBody Map<String, String> request) {
        return notificationClient.sendNotification(
                request.get("email"),
                request.getOrDefault("text", "Тестовое уведомление")
        );
    }
}