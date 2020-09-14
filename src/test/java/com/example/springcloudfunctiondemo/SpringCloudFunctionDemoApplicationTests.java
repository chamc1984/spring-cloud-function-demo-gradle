package com.example.springcloudfunctiondemo;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.springcloudfunctiondemo.function.ApiGatewayEventProxyIntegrationFunction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringCloudFunctionDemoApplicationTests {

	@Test
	public void HelloTest1() {
		ApiGatewayEventProxyIntegrationFunction function = new ApiGatewayEventProxyIntegrationFunction();
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

		Map<String, String> queryStringParameter = new HashMap<>();
		queryStringParameter.put("queryparam", "11111");
		request.setQueryStringParameters(queryStringParameter);

		APIGatewayProxyResponseEvent response = function.apply(request);

		assertThat(response.getBody()).isEqualTo("queryString is : 11111. PathParam is : ");
	}

}
