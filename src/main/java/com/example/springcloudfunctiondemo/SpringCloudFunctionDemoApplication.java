package com.example.springcloudfunctiondemo;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.springcloudfunctiondemo.function.ApiGatewayEventProxyIntegrationFunction;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.FunctionType;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication(proxyBeanMethods = false)
public class SpringCloudFunctionDemoApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        FunctionalSpringApplication.run(SpringCloudFunctionDemoApplication.class, args);
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean(
                "apiGatewayEventProxyIntegrationFunction",
                FunctionRegistration.class,
                () -> new FunctionRegistration<>(
                        new ApiGatewayEventProxyIntegrationFunction()
                ).type(
                        FunctionType.from(APIGatewayProxyRequestEvent.class)
                                .to(APIGatewayProxyResponseEvent.class)
                )
        );
    }
}
