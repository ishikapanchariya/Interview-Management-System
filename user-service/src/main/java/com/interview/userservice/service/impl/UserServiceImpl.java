package com.interview.userservice.service.impl;

import com.interview.userservice.constants.MessageConstants;
import com.interview.userservice.dto.request.LoginRequest;
import com.interview.userservice.dto.request.RegisterRequest;
import com.interview.userservice.dto.response.ApiResponse;
import com.interview.userservice.dto.response.LoginResponse;
import com.interview.userservice.entity.User;
import com.interview.userservice.enums.Role;
import com.interview.userservice.exception.InvalidCredentialsException;
import com.interview.userservice.exception.UserAlreadyExistsException;
import com.interview.userservice.mapper.UserMapper;
import com.interview.userservice.repository.UserRepository;
import com.interview.userservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResponse<Void> register(RegisterRequest request) {

        // Step 1: Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException(MessageConstants.USER_ALREADY_EXISTS);
        }
        // Step 2: Convert DTO to Entity
        User user = userMapper.toEntity(request);

        // Step 3: Encode Password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Step 4: Set Default Role
        user.setRole(Role.ROLE_CANDIDATE);

        // Step 5: Save User
        userRepository.save(user);

        // Step 6: Return Response
        return ApiResponse.<Void>builder()
                .success(true)
                .message(MessageConstants.USER_REGISTERED_SUCCESS)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

    }
    @Override
    public ApiResponse<LoginResponse> login(LoginRequest request) {

        // Step 1: Find User by Email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->
                        new InvalidCredentialsException(MessageConstants.INVALID_CREDENTIALS));

        //Verify password
       if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new InvalidCredentialsException(MessageConstants.INVALID_CREDENTIALS);
        }

        // Step 3 - Temporary Token
       String token = null;

        // Step 4 - Prepare Response
       LoginResponse loginResponse = userMapper.toLoginResponse(user);
            //only token manually set karenge
        loginResponse.setAccessToken(token);
        loginResponse.setTokenType("Bearer");

        // Step 5 - Return Response
        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .message(MessageConstants.USER_LOGIN_SUCCESS)
                .data(loginResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
