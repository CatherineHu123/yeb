package com.xpz.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xpz.pojo.MailConstants;
import com.xpz.pojo.MailLog;
import com.xpz.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitMQ配置类
 *
 * @Author: Catherine
 */
@Configuration
public class RabbitMQConfig {
    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 消息确认回调，确定消息是否到达broker
         * data：唯一标识符，之前的UUID
         * ack：布尔类型，
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback((data, ack, cause)->{
            String msgId = data.getId();
            if (ack){
                LOGGER.info("{}========>消息发送成功！",msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",1).eq("msgId",msgId));
            }else {
                LOGGER.error("消息发送失败的原因==========》{}！",cause);
            }
        });
        /**
         * 消息失败回调，比如routing未达到queue
         * 旧版setReturnCallback过期了（包括message：消息主题,repText, repCode,exchange,routingkey)
         * 新版只包括returnedMessage,不过方面的信息都可以get到
         */
        rabbitTemplate.setReturnsCallback((returnedMessage)->{
            LOGGER.error("{}========>消息发送失敗！",returnedMessage.getMessage());
        });
        return rabbitTemplate;
    }

    @Bean
    public Queue queue(){
        return new Queue(MailConstants.Mail_QUEUE_NAME);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MailConstants.Mail_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.Mail_ROUTING_KEY_NAME);
    }
}
