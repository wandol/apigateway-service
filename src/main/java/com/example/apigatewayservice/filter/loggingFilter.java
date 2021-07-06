package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class loggingFilter extends AbstractGatewayFilterFactory<loggingFilter.Config> {

    public loggingFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Global filter baseMessage :  {}", config.getBaseMessage());
//
//            if(config.isPreLogger()){
//                log.info("Global filter start :  request id -> {}", request.getId());
//            }
//            //custom post filter
//            return chain.filter(exchange).then(Mono.fromRunnable(()->{
//                if(config.isPostLogger()){
//                    log.info("Global filter end : response code -> {}", response.getStatusCode());
//                }
//            }));
//        };
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging filter baseMessage :  {}", config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("Logging filter start :  request id -> {}", request.getId());
            }
            //custom post filter
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if(config.isPostLogger()){
                    log.info("Logging filter end : response code -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.LOWEST_PRECEDENCE);
        return filter;
    }
    @Data
    public static class Config{
        // put the configuration properties

        private boolean postLogger;
        private String baseMessage;
        private boolean preLogger;
    }

}
