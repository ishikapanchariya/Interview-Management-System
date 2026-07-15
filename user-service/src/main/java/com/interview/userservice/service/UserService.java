package com.interview.userservice.service;

import com.interview.userservice.dto.request.ChangePasswordRequest;
import com.interview.userservice.dto.request.LoginRequest;
import com.interview.userservice.dto.request.RegisterRequest;
import com.interview.userservice.dto.request.UpdateProfileRequest;
import com.interview.userservice.dto.response.ApiResponse;
import com.interview.userservice.dto.response.LoginResponse;
import com.interview.userservice.dto.response.UserResponse;
import com.interview.userservice.enums.Role;

import java.util.List;

public interface UserService {
    ApiResponse<UserResponse> register(RegisterRequest request);

    ApiResponse<LoginResponse> login(LoginRequest request);

    ApiResponse<UserResponse> getProfile();

    ApiResponse<UserResponse> updateProfile(UpdateProfileRequest request);

    ApiResponse<Void> changePassword(ChangePasswordRequest request);

    ApiResponse<UserResponse> getUserById(Long id);

    ApiResponse<List<UserResponse>> getAllUsers();

    ApiResponse<UserResponse> changeUserRole(Long id, Role role);

    ApiResponse<Void> deleteUser(Long id);

    ApiResponse<UserResponse> getInternalUser(Long id);

    ApiResponse<List<UserResponse>> getInternalUsers();
}
