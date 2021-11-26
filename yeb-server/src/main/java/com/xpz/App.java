package com.xpz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 */
@SpringBootApplication
@MapperScan("com.xpz.mapper")
@EnableScheduling
public class App
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
}
