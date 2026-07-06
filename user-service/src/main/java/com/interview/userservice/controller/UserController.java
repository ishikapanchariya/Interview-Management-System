package com.interview.userservice.controller;

import com.interview.userservice.constants.ApiConstants;
import com.interview.userservice.dto.request.ChangePasswordRequest;
import com.interview.userservice.dto.request.LoginRequest;
import com.interview.userservice.dto.request.UpdateProfileRequest;
import com.interview.userservice.dto.response.ApiResponse;
import com.interview.userservice.dto.response.UserResponse;
import com.interview.userservice.enums.Role;
import com.interview.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.USERS)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        ApiResponse<UserResponse> response = userService.getProfile();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request){
        ApiResponse<UserResponse> response = userService.updateProfile(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword
            (@Valid @RequestBody ChangePasswordRequest request){
        ApiResponse<Void> response = userService.changePassword(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById
            (@PathVariable Long id){
        ApiResponse<UserResponse> response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponse<UserResponse>> changeUserRole
            (@PathVariable Long id,@RequestParam Role role){
        return ResponseEntity.ok(userService.changeUserRole(id, role));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

}