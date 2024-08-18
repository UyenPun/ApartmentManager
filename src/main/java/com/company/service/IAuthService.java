package com.company.service;

import com.company.dto.LoginInfoDto;

public interface IAuthService {

	LoginInfoDto login(String username);
}
