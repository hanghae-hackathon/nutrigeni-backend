package org.nutrigenie.technopark.service;

import org.nutrigenie.technopark.model.FoodDetail;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FoodService {

    String foodExerciseMethod(String foodName);

    String checkHealthAndFoodConflicts(String foodName, String medicalConditions);

    List<FoodDetail> foodDetailData(String foodId);
}
