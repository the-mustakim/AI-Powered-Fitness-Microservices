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
            User existingUser = userRepository.findByEmail(request.getEmail());
            UserResponce userResponce = new UserResponce();
            userResponce.setId(existingUser.getId());
            userResponce.setKeyClockId(existingUser.getKeyClockId());
            userResponce.setEmail(existingUser.getEmail());
            userResponce.setFirstName(existingUser.getFirstName());
            userResponce.setLastName(existingUser.getLastName());
            userResponce.setPassword(existingUser.getPassword());
            userResponce.setCreatedAt(existingUser.getCreatedAt());
            userResponce.setUpdatedAt(existingUser.getUpdatedAt());
            return userResponce;
        }

        User user = new User();
        user.setKeyClockId(request.getKeyClockId());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = userRepository.saveAndFlush(user);
        UserResponce userResponce = new UserResponce();
        userResponce.setId(savedUser.getId());
        userResponce.setKeyClockId(savedUser.getKeyClockId());
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
        userResponce.setKeyClockId(user.getKeyClockId());
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
        return userRepository.existsByKeyClockId(userId);
    }
}
