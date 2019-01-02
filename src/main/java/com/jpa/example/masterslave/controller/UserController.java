package com.jpa.example.masterslave.controller;

import com.jpa.example.masterslave.model.UserEntity;
import com.jpa.example.masterslave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/master/create", method = RequestMethod.POST)
    public ResponseEntity<UserEntity> createUserInDbMaster(@RequestBody UserEntity userEntity) {
        return ResponseEntity.ok(userService.createUserInDbMaster(userEntity));
    }

    @RequestMapping(value = "/slave/create", method = RequestMethod.POST)
    public ResponseEntity<UserEntity> createUserInDbSlave(@RequestBody UserEntity userEntity) {
        return ResponseEntity.ok(userService.createUserInDbSlave(userEntity));
    }

    @RequestMapping(value = "/master/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserEntity> getUserFromDbMaster(@RequestParam("id") Long id) {
        return ResponseEntity.ok(userService.getUserFromDbMaster(id));
    }

    @RequestMapping(value = "/slave/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserEntity> getUserFromDbSlave(@RequestParam("id") Long id) {
        return ResponseEntity.ok(userService.getUserFromDbSlave(id));
    }


}
