package com.ms.email.consumer;

import com.ms.email.dto.EmailRecordDto;
import com.ms.email.entity.EmailEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailRecordDto emailRecordDto) {
        var emailEntity = new EmailEntity();
        BeanUtils.copyProperties(emailRecordDto, emailEntity);
    }
}
