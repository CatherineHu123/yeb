package com.xpz;

import com.xpz.pojo.MailConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 启动类
 *
 * @Author: Catherine
 */
@SpringBootApplication
public class MailApp {
    public static void main(String[] args) {
        SpringApplication.run(MailApp.class, args);
    }
}
