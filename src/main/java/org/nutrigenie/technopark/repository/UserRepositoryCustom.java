package org.nutrigenie.technopark.repository;

import org.nutrigenie.technopark.model.User;

import javax.transaction.Transactional;

@Transactional
public interface UserRepositoryCustom {

    User findByEmail(String email);

    void updateUserRefreshToken(User paramUser);

    long logOut(long id);

}
