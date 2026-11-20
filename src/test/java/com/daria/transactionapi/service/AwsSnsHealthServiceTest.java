package com.daria.transactionapi.service;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ListTopicsResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

class AwsSnsHealthServiceTest {

    @Test
    void shouldReturnNumberOfSnsTopics() {
        SnsClient snsClient = mock(SnsClient.class);

        ListTopicsResponse response = ListTopicsResponse.builder()
                // .topics() confused compiler between two overloaded methods provided by ListTopicsResponse.Builder
                // #1. topics(Collection<Topic> topics)
                // #2. topics(Topic... topics)
                // modified to pass an explicit empty List
                .topics(Collections.emptyList())
                .build();

        when(snsClient.listTopics()).thenReturn(response);

        AwsSnsHealthService service =
                new AwsSnsHealthService(snsClient);

        int topicCount = service.getTopicCount();

        assertEquals(0, topicCount);
    }
}