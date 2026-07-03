package com.interview.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    @NotBlank
    private Long userId;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

}
