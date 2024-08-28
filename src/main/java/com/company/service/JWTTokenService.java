package com.company.service;

import com.company.adaptor.database.repository.ITokenRepository;
import com.company.domain.entity.Token;
import com.company.domain.entity.Token.Type;
import com.company.domain.entity.User;
import com.company.presentation.rest.auth.response.TokenDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

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

    @Value("${jwt.refreshtoken.time.expiration}")
    private long REFRESH_EXPIRATION_TIME;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITokenRepository tokenRepository;

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

    @Override
    @Transactional
    public Token generateRefreshToken(User user) {
        Token refreshToken = new Token(user, UUID.randomUUID().toString(), Token.Type.REFRESH_TOKEN,
                new Date(new Date().getTime() + REFRESH_EXPIRATION_TIME));

        // delete all old refresh token of this account
        tokenRepository.deleteByUser(user);

        // create new token
        return tokenRepository.save(refreshToken);
    }

    //	Check refresh token xem có đúng đã cấp và còn hạn không
    @Override
    public boolean isRefreshTokenValid(String refreshToken) {
        Token entity = tokenRepository.findBykeyAndType(refreshToken, Type.REFRESH_TOKEN);
        return entity != null && !entity.getExpiredDate().before(new Date());
    }

    @Override
    @Transactional
    public TokenDTO getNewToken(String refreshToken) {
        // find old refresh token
        Token oldRefreshToken = tokenRepository.findBykeyAndType(refreshToken, Type.REFRESH_TOKEN); // get thông tin token cũ

        // delete old refresh token
        tokenRepository.deleteByUser(oldRefreshToken.getUser()); // delete token cũ đi (chỉ lưu refresh token mới ở database)

        // create new refresh token
        Token newRefreshToken = generateRefreshToken(oldRefreshToken.getUser()); // create token & refresh token

        // create new jwt token
        String newToken = generateJWTToken(oldRefreshToken.getUser().getUsername());

        return new TokenDTO(newToken, newRefreshToken.getKey());
    }
}