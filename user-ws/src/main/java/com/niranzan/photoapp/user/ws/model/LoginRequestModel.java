package com.niranzan.photoapp.user.ws.model;

import lombok.Data;

@Data
public class LoginRequestModel {
    private String email;
    private String password;
}
