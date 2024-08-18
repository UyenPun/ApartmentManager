package com.company.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String role;
    private String token;
}
