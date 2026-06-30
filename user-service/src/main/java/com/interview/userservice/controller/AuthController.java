package com.interview.userservice.controller;

import com.interview.userservice.service.UserService;

public class AuthController {
    public final UserService userService;

    public AuthController(UserService userService){
        this.userService=userService;
    }
}
