package com.example.astonspringrest;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Service
public class NotificationClient {

    private final RestClient restClient;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    public NotificationClient(
            RestClient.Builder restClientBuilder,
            Resilience4JCircuitBreakerFactory circuitBreakerFactory,
            @Value("${notification.service.url}") String notificationServiceUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(notificationServiceUrl)
                .build();

        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public String sendNotification(String email, String text) {
        return circuitBreakerFactory
                .create("notificationService")
                .run(
                        () -> restClient.post()
                                .uri("/api/notifications/send")
                                .body(Map.of(
                                        "email", email,
                                        "text", text
                                ))
                                .retrieve()
                                .toBodilessEntity()
                                .toString(),

                        throwable -> {
                            System.out.println(
                                    "Notification service unavailable: "
                                            + throwable.getMessage()
                            );
                            return "Notification skipped";
                        }
                );
    }
}