/* Creating an AWS configuration class

SnsClient.builder()
    - Creating the AWS SDK's service client.

.region(Region.CA_CENTRAL_1)
    - Tells the client which AWS region it should communicate with.

.build();
    - Creates the client.
    Spring then manages that client as a bean: Spring -> SnsClient
    Other application components can receive it through constructor injection.

The AWS SDK documentation describes service clients as the mechanism used to make AWS service requests
*/

package com.daria.transactionapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsConfig {

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(Region.CA_CENTRAL_1)
                .build();
    }
}