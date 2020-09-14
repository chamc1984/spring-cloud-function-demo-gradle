package com.example.springcloudfunctiondemo.function;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.function.Function;

/**
 * ファンクションクラス.<br />
 * <br />
 * ハンドラから呼び出される処理を記載するクラス<br />
 */
@Slf4j
@Component
public class ApiGatewayEventProxyIntegrationFunction
        implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent input) {
        log.info("Start function.");

        // Get queryString parameter
        Map<String, String> queryStringParameter = input.getQueryStringParameters();
        String queryParam = "";
        if (!CollectionUtils.isEmpty(queryStringParameter)) {
            queryParam = queryStringParameter.get("queryparam");
            log.info("Parameter : " + queryParam);
        } else {
            log.error("Missing QueryString Parameter.");
        }

        // Get path parameter
        Map<String, String> pathParameter = input.getPathParameters();
        String pathParam = "";
        if (!CollectionUtils.isEmpty(pathParameter)) {
            pathParam = pathParameter.get("pathparam");
            log.info("PathParam:" + pathParam);
        } else {
            log.error("Missing Path Parameter.");
        }

        // Create response object
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setBody("queryString is : " + queryParam + ". PathParam is : " + pathParam);
        return response;
    }
}
