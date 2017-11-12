package com.mj.aws.lambda.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.DeleteMessageBatchResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.google.common.base.Preconditions;
import com.mj.aws.lambda.sqs.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Component("awsLambdaSqsFunction")
public class AwsLambdaSqsFunction implements Function<Void,Void>{

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private AmazonSQS amazonSqs;

    @Override
    public Void apply(Void empty) {

        String queueUrl = appConfig.getQueueUrl();

        List<Message> messageList = this.getQueueMessageByQueueUrl(queueUrl);

        for (Message message : messageList) {
            log.info("Received message: {}", message.toString());
        }

        this.deleteMessagesByQueueUrl(messageList,queueUrl);

        return empty;
    }

    private void deleteMessagesByQueueUrl(List<Message> messageList, String queueUrl) {

        Preconditions.checkArgument(!messageList.isEmpty(),"Empty list for removal.");

        List<DeleteMessageBatchRequestEntry> deleteMessageEntries = new ArrayList<>();

        messageList.forEach(message ->
            deleteMessageEntries.add(
                    new DeleteMessageBatchRequestEntry(message.getMessageId(),message.getReceiptHandle())
        ));

        DeleteMessageBatchRequest deleteRequest = new DeleteMessageBatchRequest();
        deleteRequest.setQueueUrl(queueUrl);
        deleteRequest.setEntries(deleteMessageEntries);

        DeleteMessageBatchResult result = amazonSqs.deleteMessageBatch(deleteRequest);

        log.info("Successfully removed size: {}",result.getSuccessful().size());
        log.error("Failed to removed size: {}",result.getFailed().size());
    }

    private List<Message> getQueueMessageByQueueUrl(String queueUrl) {
        log.info("Getting messages from queue url: {}", queueUrl);

        ReceiveMessageRequest messageRequest = new ReceiveMessageRequest(queueUrl).
                withWaitTimeSeconds(5).withMaxNumberOfMessages(2);

        List<Message> messages = amazonSqs.receiveMessage(messageRequest).getMessages();

        log.info("Received total messages size: {}", messages.size());

        return messages;
    }


}
