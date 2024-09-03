package com.company.adaptor.database.form;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatingUserForm {

    @NotBlank(message = "Username must not be null or empty")
    @Size(max = 255, message = "Username length must be less than or equal to 255 characters")
    private String username;

    @NotBlank(message = "Email must not be null or empty")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email length must be less than or equal to 255 characters")
    private String email;

    @NotBlank(message = "Password must not be null or empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Role must not be null")
    @Pattern(regexp = "ADMIN|MANAGER|RESIDENT", message = "Role must be one of ADMIN, MANAGER, or RESIDENT")
    private String role;

}
