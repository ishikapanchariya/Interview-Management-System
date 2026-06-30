package com.interview.userservice.dto.response;

import com.interview.userservice.entity.User;
import com.interview.userservice.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
}
