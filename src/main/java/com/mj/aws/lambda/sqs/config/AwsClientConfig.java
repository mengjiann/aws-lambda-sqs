package com.mj.aws.lambda.sqs.config;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsClientConfig {

    @Bean
    public AmazonSQS awsSQSClient(){
        return AmazonSQSClientBuilder.defaultClient();
    }


}
