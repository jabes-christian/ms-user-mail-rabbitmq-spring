package com.ms.user.service;

import com.ms.user.entity.UserEntity;
import com.ms.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
