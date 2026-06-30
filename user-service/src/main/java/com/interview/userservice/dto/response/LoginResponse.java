package com.interview.userservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;  // JWT generated after successful login
    private String tokenType; // Bearer
    private String role; // User role(Candidate, HR, Admin)
    private String name;
}
