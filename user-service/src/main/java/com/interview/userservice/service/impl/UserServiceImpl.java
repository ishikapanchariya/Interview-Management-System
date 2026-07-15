package com.interview.userservice.service.impl;

import com.interview.userservice.constants.MessageConstants;
import com.interview.userservice.dto.request.ChangePasswordRequest;
import com.interview.userservice.dto.request.LoginRequest;
import com.interview.userservice.dto.request.RegisterRequest;
import com.interview.userservice.dto.request.UpdateProfileRequest;
import com.interview.userservice.dto.response.ApiResponse;
import com.interview.userservice.dto.response.LoginResponse;
import com.interview.userservice.dto.response.UserResponse;
import com.interview.userservice.entity.User;
import com.interview.userservice.enums.Role;
import com.interview.userservice.exception.InvalidCredentialsException;
import com.interview.userservice.exception.ResourceNotFoundException;
import com.interview.userservice.exception.UserAlreadyExistsException;
import com.interview.userservice.exception.UserNotFoundException;
import com.interview.userservice.mapper.UserMapper;
import com.interview.userservice.repository.UserRepository;
import com.interview.userservice.security.JwtService;
import com.interview.userservice.security.UserPrincipal;
import com.interview.userservice.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ApiResponse<UserResponse> register(RegisterRequest request) {

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
        User savedUser = userRepository.save(user);

        //Conversion Entity to ResponseDTO
        UserResponse userResponse =  userMapper.toUserResponse(savedUser);

        // Step 6: Return Response
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message(MessageConstants.USER_REGISTERED_SUCCESS)
                .data(userResponse)
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
        // Step 3 - Login API Token
       String token = jwtService.generateToken(user);
        // Step 4 - Prepare Response
       LoginResponse loginResponse = userMapper.toLoginResponse(user);
            //only token manually set Karenge
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
    //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpc2hhQGdtYWlsLmNvbSIsImlhdCI6MTc4MzMzNTM5OSwiZXhwIjoxNzgzNDIxNzk5fQ.bjnlwJCWQ4-Dljf_k6YRpOi8ZDn9-a_-r8YyeHLW0ts

    @Override
    public ApiResponse<UserResponse> getProfile() {

        //Find email of logged-in user(Won't work w.o. Authentication)
        String email =
                SecurityContextHolder.getContext().getAuthentication().getName();
        //User dhundho
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new UserNotFoundException(MessageConstants.USER_NOT_FOUND));
        //Entity ko Response DTO m convert
        UserResponse userResponse =  userMapper.toUserResponse(user);

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message(MessageConstants.USER_FETCHED_SUCCESS)
                .data(userResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<UserResponse> updateProfile(UpdateProfileRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

        userMapper.updateUserFromRequest(request, user);

        User updatedUser = userRepository.save(user);
        UserResponse userResponse =  userMapper.toUserResponse(updatedUser);

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message(MessageConstants.USER_UPDATED_SUCCESS)
                .data(userResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<Void> changePassword(ChangePasswordRequest request) {

        // Get logged-in user's email from JWT

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getOldPassword(),user.getPassword())){
            throw new InvalidCredentialsException(MessageConstants.INVALID_OLD_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ApiResponse.<Void>builder()
                .success(true)
                .message(MessageConstants.PASSWORD_CHANGED_SUCCESS)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<UserResponse> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(MessageConstants.USER_NOT_FOUND));
        UserResponse userResponse =  userMapper.toUserResponse(user);
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message(MessageConstants.USER_FETCHED_SUCCESS)
                .data(userResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll()
                .stream().map(userMapper::toUserResponse)
                .toList();

        return ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .message(MessageConstants.USERS_FETCHED_SUCCESS)
                .data(users)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<UserResponse> changeUserRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(()->
                        new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

        user.setRole(role);
        User updateduser = userRepository.save(user);
        UserResponse userResponse =  userMapper.toUserResponse(updateduser);

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message(MessageConstants.ROLE_UPDATED_SUCCESS)
                .data(userResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<Void> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

        userRepository.delete(user);

        return ApiResponse.<Void>builder()
                .success(true)
                .message(MessageConstants.USER_DELETED_SUCCESS)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<UserResponse> getInternalUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        UserResponse response =  userMapper.toUserResponse(user);
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Internal user fetched successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<List<UserResponse>> getInternalUsers() {
        List<UserResponse> responses = userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
        return ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .message("User fetched successfully")
                .data(responses)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
