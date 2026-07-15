package com.interview.userservice.controller;

import com.interview.userservice.constants.ApiConstants;
import com.interview.userservice.dto.response.ApiResponse;
import com.interview.userservice.dto.response.UserResponse;
import com.interview.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.INTERNAL_USERS)
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getInternalUser
            (@PathVariable Long id){
        return ResponseEntity.ok(userService.getInternalUser(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getInternalUsers(){
        return ResponseEntity.ok(userService.getInternalUsers());
    }

}
