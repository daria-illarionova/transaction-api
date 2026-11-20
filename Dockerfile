FROM public.ecr.aws/lambda/java:25

COPY target/classes ${LAMBDA_TASK_ROOT}

COPY target/dependency/* ${LAMBDA_TASK_ROOT}/lib/

CMD [ "com.daria.transactionapi.lambda.TransactionLambdaHandler::handleRequest" ]