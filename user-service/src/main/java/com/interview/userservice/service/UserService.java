package com.interview.userservice.service;

import com.interview.userservice.dto.request.LoginRequest;
import com.interview.userservice.dto.request.RegisterRequest;
import com.interview.userservice.dto.response.ApiResponse;
import com.interview.userservice.dto.response.LoginResponse;

public interface UserService {
    ApiResponse<Void> register(RegisterRequest request);
    ApiResponse<LoginResponse> login(LoginRequest request);
}
