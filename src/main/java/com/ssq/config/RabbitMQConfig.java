package com.ssq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_DIRECT_INFORM="exchange_direct_inform";
    public static final String QUEUE_INFORM_BLOG="queue_inform_blog";
    public static final String ROUTINGKEY_BLOG="routingkey_blog";


    //声明交换机
    @Bean(EXCHANGE_DIRECT_INFORM)
    public Exchange exchange_direct_inform(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.directExchange(EXCHANGE_DIRECT_INFORM).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_INFORM_BLOG)
    public Queue queue_inform_blog(){
        return new Queue(QUEUE_INFORM_BLOG);
    }

    //绑定队列和交换机
    @Bean
    public Binding binging_routingkey_blog(@Qualifier(QUEUE_INFORM_BLOG) Queue queue,
                                           @Qualifier(EXCHANGE_DIRECT_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_BLOG).noargs();
    }

}
