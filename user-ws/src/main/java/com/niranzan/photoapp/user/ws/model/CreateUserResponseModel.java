package com.niranzan.photoapp.user.ws.model;

import lombok.Data;

@Data
public class CreateUserResponseModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
}
