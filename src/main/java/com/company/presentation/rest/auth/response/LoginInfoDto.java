package com.company.presentation.rest.auth.response;

import lombok.Data;

@Data
public class LoginInfoDto {

    private Integer id;

    private String username;

    private String role;

    private String token;

    private String refreshToken;

}
