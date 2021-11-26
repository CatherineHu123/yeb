package com.xpz.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xpz.mapper.EmployeeMapper;
import com.xpz.pojo.Employee;
import com.xpz.pojo.MailConstants;
import com.xpz.pojo.MailLog;
import com.xpz.service.IEmployeeService;
import com.xpz.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时发送任务
 *
 * @Author: Catherine
 */
@Component
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 定时任务，每10秒发送一次
    @Scheduled(cron = "*/5 * * * * ?")
    public void mailTask(){
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>().
                eq("status", 0).
                lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            // 如果尝试超过3次，则将状态改为投递失败，并不在尝试
            if (mailLog.getCount()>=3){
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",2).eq("msgId",mailLog.getMsgId()));
                return;
            }
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count",mailLog.getCount()+1)
                    .set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.Mail_TIMEOUT))
                    .set("updateTime", LocalDateTime.now())
                    .eq("msgId",mailLog.getMsgId()));
            Employee employee = employeeMapper.getEmployee(mailLog.getEid()).get(0);
            rabbitTemplate.convertAndSend("MailConstants.Mail_EXCHANGE_NAME", MailConstants.Mail_ROUTING_KEY_NAME,
                    employee, new CorrelationData(mailLog.getMsgId()));
        });
    }
}
