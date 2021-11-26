package com.xpz.pojo;

/**
 * 消息状态
 *
 * @Author: Catherine
 */
public class MailConstants {
    // 消息投递中
    public static final Integer DELIVERING = 0;
    // 投递成功
    public static final Integer SUCCESS = 1;
    // 投递失败
    public static final Integer FAILURE = 2;
    // 最大尝试次数
    public static final Integer Mail_TYR_COUNT = 3;
    // 超时时间
    public static final Integer Mail_TIMEOUT = 1;
    // 消息队列
    public static final String Mail_QUEUE_NAME = "mail.queue";
    // 路由键
    public static final String Mail_ROUTING_KEY_NAME = "mail.routing.name";
    // 交换机
    public static final String Mail_EXCHANGE_NAME = "mail.exchange.name";
}
