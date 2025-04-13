package com.CarStoreRestApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CarStoreRestApi.dto.UserDto;
import com.CarStoreRestApi.model.User;
import com.CarStoreRestApi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userserviceobj;

    @PostMapping("/add")
    public ResponseEntity<String> adduser(@RequestBody User userobj) {
        String message = userserviceobj.saveuser(userobj);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/fetchdata")
    public List<User> fetchdata() {
        return userserviceobj.fetchdata();
    }

    @PutMapping("/updatedata/{id}")
    public ResponseEntity<String> updatedata(@PathVariable int id, @RequestBody User userobj) {
        userserviceobj.updatedata(id, userobj);
        return ResponseEntity.ok("Data updated successfully");
    }

    @DeleteMapping("/deletedata/{id}")
    public ResponseEntity<String> deletedata(@PathVariable int id) {
        userserviceobj.deletedata(id);
        return ResponseEntity.ok("Data deleted");
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDto userDto) {
        User user = userserviceobj.registerUser(userDto);
        return ResponseEntity.ok(user);
    }
}
