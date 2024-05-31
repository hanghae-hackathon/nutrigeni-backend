package org.nutrigenie.technopark.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nutrigenie.technopark.dto.TokenInfoDTO;
import org.nutrigenie.technopark.dto.UserDTO;
import org.nutrigenie.technopark.exception.UserForBiddenException;
import org.nutrigenie.technopark.model.User;
import org.nutrigenie.technopark.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API",
            description = "비밀번호 및 이메일 유효성 체크로직에 유배 할 경우 valid 변수 리턴, " +
                    "체크 로직 통과시 User Model 리턴")
    @PostMapping("/create-user")
//    public ResponseEntity<?> createUser(@RequestBody @Valid User paramUser, BindingResult bindingResult){
        public ResponseEntity<?> createUser(@RequestBody User paramUser){
//        if(bindingResult.hasErrors()){
//            Map<String, String> validatorResult = userService.validateHandling(bindingResult);
//            System.out.println(validatorResult.toString());
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(validatorResult);
//        }

        User user = userService.findUserByEmail(paramUser.getEmail());
        if(user == null){
            User saveUser = userService.createUser(paramUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(saveUser);
        }else{
            throw new UserForBiddenException("ID:" + paramUser.getUserName() + " Error Duplication");
        }
    }


    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public TokenInfoDTO login(@RequestBody UserDTO.Login loginDTO){

        TokenInfoDTO tokenInfoDTO = userService.login(loginDTO);

        if(tokenInfoDTO != null){
            //유저의 refreshToken 값 update
            userService.updateRefreshToken(tokenInfoDTO.getUser());
            return tokenInfoDTO;
        }

        return null;

    }



}
