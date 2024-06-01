package org.nutrigenie.technopark.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.nutrigenie.technopark.model.FoodDetail;
import org.nutrigenie.technopark.model.QFoodDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodDetailRepositoryCustomImpl implements FoodDetailRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FoodDetail> foodDetailData(String foodId) {

        return queryFactory.selectFrom(QFoodDetail.foodDetail)
                .where(QFoodDetail.foodDetail.uploadFood.id.eq(Long.valueOf(foodId)))
                .fetch();
    }
}
