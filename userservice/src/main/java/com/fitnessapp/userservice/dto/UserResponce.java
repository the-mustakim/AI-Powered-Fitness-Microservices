package com.fitnessapp.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponce {

    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
