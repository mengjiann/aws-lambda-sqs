package com.mj.aws.lambda.sqs.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Slf4j
@Configuration
public class AwsClientConfig {

    @Bean
    @Profile("aws")
    public AmazonSQS awsSQSClient(){
        return AmazonSQSClientBuilder.defaultClient();
    }

    @Bean
    @Profile("local")
    public AmazonSQS localstackSqsClient(){
        return AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localstack:4576","eu-west-1"))
                .build();
    }


}
