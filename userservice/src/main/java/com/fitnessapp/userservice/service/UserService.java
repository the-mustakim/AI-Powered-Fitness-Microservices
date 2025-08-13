package com.fitnessapp.userservice.service;

import com.fitnessapp.userservice.dto.RegisterRequest;
import com.fitnessapp.userservice.dto.UserResponce;
import com.fitnessapp.userservice.model.User;
import com.fitnessapp.userservice.respository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public UserResponce register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exist");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        User savedUser = userRepository.saveAndFlush(user);

        UserResponce userResponce = new UserResponce();
        userResponce.setId(savedUser.getId());
        userResponce.setEmail(savedUser.getEmail());
        userResponce.setFirstName(savedUser.getFirstName());
        userResponce.setLastName(savedUser.getLastName());
        userResponce.setPassword(savedUser.getPassword());
        userResponce.setCreatedAt(savedUser.getCreatedAt());
        userResponce.setUpdatedAt(savedUser.getUpdatedAt());
        return userResponce;
    }

    public UserResponce getUserProfile(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found"));

        UserResponce userResponce = new UserResponce();
        userResponce.setId(user.getId());
        userResponce.setEmail(user.getEmail());
        userResponce.setFirstName(user.getFirstName());
        userResponce.setLastName(user.getLastName());
        userResponce.setPassword(user.getPassword());
        userResponce.setCreatedAt(user.getCreatedAt());
        userResponce.setUpdatedAt(user.getUpdatedAt());
        return userResponce;
    }

    public Boolean existByUserId(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return userRepository.existsById(userId);
    }
}
