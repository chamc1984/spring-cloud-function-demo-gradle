package com.example.springcloudfunctiondemo.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.reactivestreams.Publisher;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import reactor.core.publisher.Flux;

/**
 * ハンドラクラス.<br />
 * <br />
 * Function（関数）のエントリポイントとなるクラス<br />
 * ファンクションクラスとの紐付けは命名を統一することで自動紐付けさせる<br />
 */
public class ApiGatewayEventProxyIntegrationHandler extends SpringBootRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    /**
     * ハンドラメソッド.<br />
     * <br />
     * 本来、シンプルな使い方ならメソッドのオーバーライドは不要（extends だけで、非実装で動く）だが、<br />
     * 実際、非実装だと LinkedHashMap から APIGatewayProxyRequestEvent への ClassCastException が出てしまう<br />
     * このため、handleRequest をOverride して Exception を回避する<br />
     * <br />
     * 具体的には、
     * org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler
     * の handleRequest メソッドをベースに不要な（なくても動く）部分を削除した形とする<br />
     */
    @Override
    public Object handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        this.initialize(context);
        Object input = this.acceptsInput() ? this.convertEvent(event) : "";
        Publisher<?> output = this.apply(Flux.just(input));
        return this.result(input, output);
    }

}
