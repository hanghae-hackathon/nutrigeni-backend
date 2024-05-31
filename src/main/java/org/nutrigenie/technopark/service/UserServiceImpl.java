package org.nutrigenie.technopark.service;

import lombok.RequiredArgsConstructor;
import org.nutrigenie.technopark.auth.JwtTokenProvider;
import org.nutrigenie.technopark.dto.TokenInfoDTO;
import org.nutrigenie.technopark.dto.UserDTO;
import org.nutrigenie.technopark.model.User;
import org.nutrigenie.technopark.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public User createUser(User paramUser) {

        String encodePassword = bCryptPasswordEncoder.encode(paramUser.getPassword());
        paramUser.setPassword(encodePassword);
        paramUser.setAuthorityLevel(0);
        paramUser.setRole("admin");

        User saveUser = userRepository.save(paramUser);
        saveUser.setPassword("");

        return saveUser;

    }

    @Override
    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public TokenInfoDTO login(UserDTO.Login loginDTO) {

        // 1. Login Email/PW 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 생성
        TokenInfoDTO tokenInfo = jwtTokenProvider.generateToken(authentication);

        //4. TokenDTO 객체 리턴
        return tokenInfo;
    }

    @Override
    public int updateRefreshToken(User user) {

        userRepository.updateUserRefreshToken(user);
        return 1;
    }

    @Override
    public long logOut(long id) {
        return userRepository.logOut(id);
    }

}
