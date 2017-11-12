# aws-lambda-sqs
A sample Java AWS Lambda function to listen to AWS SQS queue and receive messages from the queue.

# Guide
- Create a new AWS SQS queue or use any existing queues.
- Update the value for aws.sqs.queue.url in the application.properties to your queue url.
- Build the project using: mvn clean install
- Create and configure using the aws lambda requirement below.
- Upload the jar file with suffix "-aws" to AWS lambda.
- Update the handler field with: com.mj.aws.lambda.sqs.AwsLambdaSqsFunctionHandler
- Trigger the Lambda function and check the log from Cloud Watch.

# AWS Lambda Requirements:
- 256 MB of Memory. (Can try with lower one)
- Timeout 1 min.
- Execution role
-- Access to SQS Policy
-- Access to Cloud watch for logging Policy

# AWS SQS 
- You can choose FIFO queue.

# Libraries
- Spring Cloud Function: https://spring.io/blog/2017/07/05/introducing-spring-cloud-function
- AWS Java SDK
- Lombok
- Log4j
- Jackson Object Mapper
- Maven

# Credits
Based on https://dzone.com/articles/run-code-with-spring-cloud-function-on-aws-lambda
