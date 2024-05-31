package org.nutrigenie.technopark.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.nutrigenie.technopark.model.QUser;
import org.nutrigenie.technopark.model.User;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public User findByEmail(String email) {

        return queryFactory.selectFrom(QUser.user)
                .where(QUser.user.email.eq(email))
                .fetchOne();

    }

    @Override
    public void updateUserRefreshToken(User paramUser) {

        queryFactory.update(QUser.user)
                .set(QUser.user.currentRefreshToken, paramUser.getCurrentRefreshToken())
                .where(QUser.user.id.eq(paramUser.getId()))
                .execute();
    }

}
