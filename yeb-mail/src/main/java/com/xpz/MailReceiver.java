package com.xpz;

import com.rabbitmq.client.Channel;
import com.xpz.pojo.Employee;
import com.xpz.pojo.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * 消息接收者
 *
 * @Author: Catherine
 */
@Component
public class MailReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String mail;

    @RabbitListener(queues = MailConstants.Mail_QUEUE_NAME)
    public void handler(Message message, Channel channel){
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        // 消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 获取Message中存入的correlationData的id，也就是msgId
        String msgId = ((String) headers.get("spring_returned_message_correlation"));
        // redis, 采用hash存储
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            if (hashOperations.entries("mail_log").containsKey(msgId)){
                LOGGER.error("{}=============》已经被消费过了",msgId);
                channel.basicAck(tag, false);
                return;
            }
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            // 发件人
            helper.setFrom(mailProperties.getUsername());
            // 收件人
            helper.setTo(mail);
            // 主题
            helper.setSubject("欢迎邮件");
            // 发送日期
            helper.setSentDate(new Date());
            // 邮件内容
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJoblevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail,true);
            // 发送邮件
            javaMailSender.send(msg);
            // 将id存入redis
            hashOperations.put("mail_log", msgId, "ok");
            // 手动确认消息
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // 第一个false：确认一条；第二个true：是否返回队列
            try {
                channel.basicNack(tag, false, true);
            } catch (IOException e1) {
                LOGGER.error("邮件发送失败=======>{}", e1.getMessage());
            }
            LOGGER.error("邮件发送失败=======>{}", e.getMessage());
        }
    }
}
