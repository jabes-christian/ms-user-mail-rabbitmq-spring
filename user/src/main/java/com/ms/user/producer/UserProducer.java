package com.ms.user.producer;

import com.ms.user.dto.EmailDto;
import com.ms.user.entity.UserEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {

    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserEntity userEntity) {
        var emailDto = new EmailDto();
        emailDto.setUserId(userEntity.getUserId());
        emailDto.setEmailTo(userEntity.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso!");
        emailDto.setText(userEntity.getName() + ", seja bem vindo(a)! \nAgradecemos o seu cadastro, aproveite agora todos os recursos da nossa plataforma!");

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }
}
