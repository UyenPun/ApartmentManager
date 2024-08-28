package com.company.service;

import com.company.presentation.rest.auth.response.LoginInfoDto;

public interface IAuthService {

    LoginInfoDto login(String username);
}
