package com.interview.userservice.mapper;

import com.interview.userservice.dto.request.RegisterRequest;
import com.interview.userservice.dto.request.UpdateProfileRequest;
import com.interview.userservice.dto.response.LoginResponse;
import com.interview.userservice.dto.response.UserResponse;
import com.interview.userservice.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest request);  // for User registration

    UserResponse toUserResponse(User user); // for User response

    User toEntity(UserResponse response); // for User response

    LoginResponse toLoginResponse(User user); // for User login

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UpdateProfileRequest request,
                               @MappingTarget User user);



}
