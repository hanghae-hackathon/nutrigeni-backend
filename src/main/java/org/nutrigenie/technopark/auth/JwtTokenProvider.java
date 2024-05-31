package org.nutrigenie.technopark.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.nutrigenie.technopark.dto.TokenInfoDTO;
import org.nutrigenie.technopark.model.User;
import org.nutrigenie.technopark.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider {


    private final Key key;
    private final long ACCESS_TOKEN_VALID_TIME = Duration.ofMinutes(20).toMillis();
    //    private final long ACCESS_TOKEN_VALID_TIME = Duration.ofSeconds(20).toMillis();
    private final long REFRESH_TOKEN_VALID_TIME = Duration.ofDays(7).toMillis();


    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfoDTO generateToken(Authentication authentication){

        User user = new User();

        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String email = userDetail.getUsername();
        long id = userDetail.getId();
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("email", email)
                .claim("id", id)
                .claim("type", "acT")
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .claim("email", email)
                .claim("id", id)
                .claim("type", "reT")
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        user.setId(id);
        user.setCurrentRefreshToken(refreshToken);
        user.setEmail(email);
        user.setRole(userDetail.getRole());

        return TokenInfoDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();

    }

    public Authentication getAuthentication(String accessToken){

        Claims claims = parseClaims(accessToken);

        if(claims.get("email") == null){
            throw new RuntimeException("권한 정보가 없는 토근입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("email").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        CustomUserDetail userDetails = new CustomUserDetail(
                claims.getSubject(),
                "",
                authorities,
                0,
                ""
        );

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

    }

    public String validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return "Success";
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            return "Invalid";
        }catch(ExpiredJwtException e){
            return "Expired";
        }catch(UnsupportedJwtException e){
            return "Unsupported";
        }catch(IllegalArgumentException e){
            return "JWT claims string is empty.";
        }

    }

    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        }catch(ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    //accessToken 재발급 로직
    public String accessTokenReissuance(User user){
        Date now = new Date();

        return Jwts.builder()
                .claim("email", user.getEmail())
                .claim("id", user.getId())
                .claim("type", "acT")
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String tokenTypeCheck(String token){

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String [] tokenSplit = token.split("\\.");
        String decodedToken = new String(decoder.decode(tokenSplit[1]), StandardCharsets.UTF_8);

        Map<String,Object> convertMap = Util.jsonToMap(decodedToken);

        return convertMap.get("type").toString();
    }

}
