package com.service.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.service.entity.User;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserDto extends User {

    private String firstName;

    private String lastName;

    private Long phoneNumber;

    private String email;
}
