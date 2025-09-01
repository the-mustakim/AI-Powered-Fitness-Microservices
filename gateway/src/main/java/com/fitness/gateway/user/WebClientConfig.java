package com.fitness.gateway.user;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /***
     * This annotation allows the service this service to resolve the service name (where we are calling) via Eureka
     * How it does?
     The @LoadBalanced annotation tells Spring Cloud to inject a load balancing filter into that builder.
     You call "http://USER-SERVICE/users".
     The load balancer filter added by @LoadBalanced intercepts the request.
     It asks Eureka for all instances of USER-SERVICE.
     Picks one instance (round robin by default).
     Rewrites the URI to something like "http://192.168.1.10:8080/users".
     Sends the request to that instance.
     * ***/
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }


    /***
     The below bean configured to point the user-service
     ***/
    @Bean
    public WebClient userServiceBuilder(WebClient.Builder webClientBuilder){
        return webClientBuilder.baseUrl("http://USER-SERVICE").build();
    }
}
