/* Proving that the SDK can make an AWS API call.

Making our first API call using SNS's ListTopics operation.
This doesn't require us to send an email. 

    Java
    ↓
    SnsClient
    ↓
    ListTopics API
    ↓
    Amazon SNS
    ↓
    response
    ↓
    Java

Using ListTopics() to learn the complete SDK request/response lifecycle 
without prematurely creating resources:
1. Construct client
2. Configure region
3. Resolve credentials
4. Create AWS request
5. Send request
6. AWS processes request
7. Receive response
8. Read response

The SNS topic and email subscription belong to the AWS infrastructure side of the project.
This infrastucture will later be defined using: 

    CDK
    ↓
    CloudFormation
    ↓
    SNS Topic
    ↓
    Email Subscription

Note: This prevents us from manually creating infrastructure that we're eventually supposed to manage as code.

*/

package com.daria.transactionapi.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
public class AwsSnsHealthService {

    private final SnsClient snsClient;

    public AwsSnsHealthService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public int getTopicCount() {
        return snsClient.listTopics()
                .topics()
                .size();
    }
}