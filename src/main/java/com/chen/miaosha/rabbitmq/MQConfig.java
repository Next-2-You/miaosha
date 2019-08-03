package com.chen.miaosha.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
	
	public static final String MIAOSHA_QUEUE = "miaosha.queue";
	public static final String MIAOSHA_EXCHANGE="miaosha_exchange";
	public static final String MIAOSHA_KEY="miaosha.key";


	@Bean
	public Queue queue() {
		return new Queue(MIAOSHA_QUEUE, true);
	}

	@Bean
	public DirectExchange directExchange(){
		return new DirectExchange(MIAOSHA_EXCHANGE);
	}
	@Bean
	public Binding topicBinding1() {
		return BindingBuilder.bind(queue()).to(directExchange()).with(MIAOSHA_KEY);
	}


}
