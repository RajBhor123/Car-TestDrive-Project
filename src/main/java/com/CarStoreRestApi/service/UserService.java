package com.CarStoreRestApi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CarStoreRestApi.dto.UserDto;
import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.model.User;
import com.CarStoreRestApi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return userRepository.save(user);
    }


    public User findBynameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow();
    }


    public String saveuser(User userobj) {
        userRepository.save(userobj);
        return "User object saved";
    }

    public List<User> fetchdata() {
        return (List<User>) userRepository.findAll();
    }

    public String updatedata(int id, User userobj) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        existingUser.setUsername(userobj.getUsername());
        existingUser.setEmail(userobj.getEmail());
        existingUser.setPassword(userobj.getPassword());
        userRepository.save(existingUser);
        return "Data updated successfully";
    }

    public String deletedata(int id) {
        userRepository.deleteById(id);
        return "Data deleted";
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }


//    public Optional<User> getUserById(Long id) {
//        return userRepository.findById(id);
//    }

    public User getCurrentUser() {
        return userRepository.findAll().stream().findFirst()
                .orElseThrow();
    }


 // Method to find a user by ID
    public User findById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null); // Return null if user is not found
    }
    
 // Fetch all customers
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}

