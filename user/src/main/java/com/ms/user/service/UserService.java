package com.ms.user.service;

import com.ms.user.entity.UserEntity;
import com.ms.user.producer.UserProducer;
import com.ms.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserRepository userRepository;
    final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    @Transactional
    public UserEntity save(UserEntity userEntity) {
        userEntity =  userRepository.save(userEntity);
        userProducer.publishMessageEmail(userEntity);
        return userEntity;
    }
}
