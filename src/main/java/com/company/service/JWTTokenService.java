package com.company.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.company.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JWTTokenService implements IJWTTokenService {

	@Value("${jwt.token.time.expiration}")
	private long EXPIRATION_TIME;

	@Value("${jwt.token.secret}")
	private String SECRET;

	@Value("${jwt.token.header.authorization}")
	private String TOKEN_AUTHORIZATION;

	@Value("${jwt.token.prefix}") // key
	private String TOKEN_PREFIX;

	@Autowired
	private IUserService userService;

	@Override
	public String generateJWTToken(String username) {
		return Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
	}

	public Authentication parseTokenToUserInformation(HttpServletRequest request) {
		String token = request.getHeader(TOKEN_AUTHORIZATION); // ở header; lấy được token ra dựa vào
																// TOKEN_AUTHORIZATION

		if (token == null) { // ko có info login
			return null;
		}

		// parse the token: Nếu quá time sẽ không parse được ra || Phải đúng SECRET và
		// còn hạn
		try {
			String username = Jwts.parser().setSigningKey(SECRET) // ở config (123456) || lấy ra được Username
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")) // xóa chữ Bearer
					.getBody().getSubject();

			User user = userService.getUserByUsername(username); // get full thông tin thằng Entity

			return username != null ? new UsernamePasswordAuthenticationToken(user.getUsername(), null, // truyền
																										// username
					AuthorityUtils.createAuthorityList(user.getRole().toString())) // các quyền của nó
					: null;
		} catch (Exception e) {
			return null;
		}
	}
}