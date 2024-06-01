package org.nutrigenie.technopark.repository;

import org.nutrigenie.technopark.model.FoodDetail;

import java.util.List;

public interface FoodDetailRepositoryCustom {

    List<FoodDetail> foodDetailData(String foodId);
}
