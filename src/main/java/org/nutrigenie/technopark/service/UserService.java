package org.nutrigenie.technopark.service;


import org.nutrigenie.technopark.dto.TokenInfoDTO;
import org.nutrigenie.technopark.dto.UserDTO;
import org.nutrigenie.technopark.model.User;
import org.springframework.validation.BindingResult;

import java.util.Map;

public interface UserService {

    User createUser(User paramUser);

    Map<String, String> validateHandling(BindingResult bindingResult);

    User findUserByEmail(String email);

    TokenInfoDTO login(UserDTO.Login loginDTO);

    int updateRefreshToken(User user);

    long logOut(long id);



}
