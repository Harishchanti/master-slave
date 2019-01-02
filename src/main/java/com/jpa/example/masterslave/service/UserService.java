package com.jpa.example.masterslave.service;

import com.jpa.example.masterslave.aop.SlaveConnection;
import com.jpa.example.masterslave.model.UserEntity;
import com.jpa.example.masterslave.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserEntity createUserInDbMaster(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @SlaveConnection
    public UserEntity createUserInDbSlave(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }


    public UserEntity getUserFromDbMaster(Long id) {
        return userRepository.findByUserId(id);
    }

    @SlaveConnection
    public UserEntity getUserFromDbSlave(Long id) {
        return userRepository.findByUserId(id);
    }


}
