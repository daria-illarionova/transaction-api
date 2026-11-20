package com.daria.transactionapi.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class TransactionLambdaHandler
        implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(
            Map<String, Object> event,
            Context context) {

        Map<String, Object> requestContext =
                (Map<String, Object>) event.get("requestContext");

        Map<String, Object> http =
                (Map<String, Object>) requestContext.get("http");

        String method = (String) http.get("method");
        String path = (String) event.get("rawPath");

        context.getLogger().log(
                "HTTP request received: " + method + " " + path
        );

        if ("GET".equals(method) && "/health".equals(path)) {
            return response(
                    200,
                    "{\"status\":\"UP\",\"service\":\"transaction-api\"}"
            );
        }

        if ("GET".equals(method) && "/test/failure".equals(path)) {
            context.getLogger().log(
                    "Controlled test failure requested"
            );

            throw new RuntimeException(
                    "Part 17 controlled Lambda failure"
            );
        }

        return response(404, "{\"error\":\"Not Found\"}");
    }

    private Map<String, Object> response(int statusCode, String body) {
        return Map.of(
                "statusCode", statusCode,
                "headers", Map.of("Content-Type", "application/json"),
                "body", body,
                "isBase64Encoded", false
        );
    }
}