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

# Guide to local deployment using sam-local and localstack
- Full guide: https://medium.com/@mengjiannlee/local-deployment-of-aws-lambda-spring-cloud-function-using-sam-local-and-localstack-dc7669110906
- Install Docker on your machine: https://www.docker.com/get-docker
- Install aws-sam-local following the guide on the github readme.
- Clone the localstack repo to your machine. Then, navigate to the local branch folder to spin up the Localstack using docker-compose: TMPDIR=/private$TMPDIR docker-compose up
  - You should seet "localstack_1 | Ready " when the localstack container is spinned up.
  - Then, you should check the network created for localstack container. The default is: localstack_default. 
    - Or you run the command: docker inspect <CONTAINER_ID> -f "{{json .NetworkSettings.Networks }}".
- Now, you will need to create messages in the queues:
  - Create queue: 
    - aws --endpoint-url=http://localhost:4576 sqs create-queue --queue-name test_queue
  - List queue:
    - aws --endpoint-url=http://localhost:4576 sqs list-queues
  - Send message: 
    - aws --endpoint-url=http://localhost:4576 sqs send-message --queue-url http://localhost:4576/queue/test_queue --message-body 'Test Message!'
- Finally, navigate to the folder where the SAM template resides and run:
  - sam local invoke AwsLambdaSqsLocal --log-file ./output.log -e event.json --docker-network <LOCALSTACK NETWORK>
    - Since the aws lambda function is executed in a docker container, it cant access the localstack deployed on the host machine. That is the reason you will need to deploy the container containing the lambda function to the same network as the localstack.
    - You can check on the output.log for debuging purpose.
    - There is also another way to pass trigger event to the lambda. You can read more from the sam-local github page.

# Guide to deploy as SAR
- Upload the sam template `template-sar.yaml` to the SAR console.
- Remember to update the `CodeUri` to point to the jar in your bucket.
- For the S3 bucket serving the `CodeUri`, the following is required for the bucket policy.
````
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": {
                "Service":  "serverlessrepo.amazonaws.com"
            },
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::<your-bucket-name>/*"
        }
    ]
}
````


# Reference:
- CLI tool for local development and testing of AWS lambda: https://github.com/awslabs/aws-sam-local
- For setting up local AWS cloud stack: https://github.com/localstack/localstack
- Reference for command: https://lobster1234.github.io/2017/04/05/working-with-localstack-command-line/
- http://bluesock.org/~willkg/blog/dev/using_localstack_for_s3.html
- http://www.frommknecht.net/spring-cloud-aws-messaging/
- https://docs.aws.amazon.com/serverlessrepo/latest/devguide/serverlessrepo-how-to-publish.html
- https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md
- https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-ref.html
- https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-api-permissions-reference.html
