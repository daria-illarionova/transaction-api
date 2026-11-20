package com.daria.transactionapi.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
public class SnsNotificationService implements NotificationService {

    private final SnsClient snsClient;

    public SnsNotificationService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    @Override
    public void sendNotification(String subject, String message) {
        // SNS publishing will be implemented after the SNS topic is created.
    }
}
