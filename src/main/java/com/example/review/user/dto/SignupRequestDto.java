package com.example.review.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}$")
    private String username;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()-_+=<>?/\\[\\]{}|;:'\",.]{4,15}$")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()-_+=<>?/\\[\\]{}|;:'\",.]{4,15}$")
    private String confirmPassword;

    @NotBlank
    @Email
    private String email;

    @Size(min = 1, max = 30)
    private String introduction;

    private String adminPW;
}
