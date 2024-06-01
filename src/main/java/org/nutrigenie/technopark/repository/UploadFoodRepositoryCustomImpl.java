package org.nutrigenie.technopark.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.nutrigenie.technopark.model.QUploadFood;
import org.nutrigenie.technopark.model.UploadFood;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UploadFoodRepositoryCustomImpl implements UploadFoodRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UploadFood> uploadFoodData() {

        return queryFactory.selectFrom(QUploadFood.uploadFood)
                .fetch();

    }
}
