package com.interview.userservice.controller;

import com.interview.userservice.constants.ApiConstants;
import com.interview.userservice.dto.request.LoginRequest;
import com.interview.userservice.dto.request.RegisterRequest;
import com.interview.userservice.dto.request.UpdateProfileRequest;
import com.interview.userservice.dto.response.ApiResponse;
import com.interview.userservice.dto.response.LoginResponse;
import com.interview.userservice.dto.response.UserResponse;
import com.interview.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.AUTH)
public class AuthController {
    public final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register
            (@Valid @RequestBody RegisterRequest request) {
        ApiResponse<UserResponse> response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse<LoginResponse> response = userService.login(request);
        return ResponseEntity.ok(response);
    }

}
