package com.induwara.store.users;

import com.induwara.store.validations.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Lowercase(message = "Email must be lowercase ")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=6, max = 25, message = "Password must be between 6-25 characters" )
    private String password;
}
