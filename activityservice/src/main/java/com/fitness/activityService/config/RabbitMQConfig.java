package com.fitness.activityService.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    /***
     This below bean decalres the new bean named activity.queue and takes the second parameter durable = true which means even if the RabbitMQ restarts
     the Queue will always be there. It won't be lost. Otherwise the RabbitMQ restarts, the queue and the message will be lost.

     * ***/
    @Bean
    public Queue activityQueue(){

        return new Queue(queueName,true);
    }

    /**
     Then we have the converted which will convert the java objects to json before sending them to RabbitMQ
     **/
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange activityExchange(){
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding activityBinding(Queue activityQueue, DirectExchange activityExchange){
        return BindingBuilder.bind(activityQueue).to(activityExchange).with(routingKey);
    }

}
