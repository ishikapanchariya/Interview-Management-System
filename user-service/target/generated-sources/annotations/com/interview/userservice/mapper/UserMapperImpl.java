package com.interview.userservice.mapper;

import com.interview.userservice.dto.request.RegisterRequest;
import com.interview.userservice.dto.request.UpdateProfileRequest;
import com.interview.userservice.dto.response.LoginResponse;
import com.interview.userservice.dto.response.UserResponse;
import com.interview.userservice.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-15T17:55:44+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.19 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( request.getName() );
        user.email( request.getEmail() );
        user.password( request.getPassword() );

        return user.build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.name( user.getName() );
        userResponse.email( user.getEmail() );
        userResponse.role( user.getRole() );

        return userResponse.build();
    }

    @Override
    public User toEntity(UserResponse response) {
        if ( response == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( response.getId() );
        user.name( response.getName() );
        user.email( response.getEmail() );
        user.role( response.getRole() );

        return user.build();
    }

    @Override
    public LoginResponse toLoginResponse(User user) {
        if ( user == null ) {
            return null;
        }

        LoginResponse.LoginResponseBuilder loginResponse = LoginResponse.builder();

        if ( user.getRole() != null ) {
            loginResponse.role( user.getRole().name() );
        }
        loginResponse.name( user.getName() );

        return loginResponse.build();
    }

    @Override
    public void updateUserFromRequest(UpdateProfileRequest request, User user) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            user.setName( request.getName() );
        }
        if ( request.getPhone() != null ) {
            user.setPhone( request.getPhone() );
        }
        if ( request.getAddress() != null ) {
            user.setAddress( request.getAddress() );
        }
    }
}
