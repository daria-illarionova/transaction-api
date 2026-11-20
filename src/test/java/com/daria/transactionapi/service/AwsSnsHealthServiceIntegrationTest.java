package com.daria.transactionapi.service;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sns.SnsClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AwsSnsHealthServiceIntegrationTest {

    @Test
    void shouldSuccessfullyCallAwsSns() {
        try (SnsClient snsClient = SnsClient.builder()
                .region(software.amazon.awssdk.regions.Region.CA_CENTRAL_1)
                .build()) {

            AwsSnsHealthService service =
                    new AwsSnsHealthService(snsClient);

            int topicCount = service.getTopicCount();

            assertNotNull(topicCount);
        }
    }
}