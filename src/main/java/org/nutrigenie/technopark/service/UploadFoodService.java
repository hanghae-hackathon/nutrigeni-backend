package org.nutrigenie.technopark.service;

import org.nutrigenie.technopark.dto.UploadFoodDTO;
import org.nutrigenie.technopark.model.FoodDetail;
import org.nutrigenie.technopark.model.UploadFood;

import java.util.List;

public interface UploadFoodService {

    UploadFood insertFoodAnalysisData(UploadFood uploadFood);

    List<UploadFood> uploadFoodData();

    FoodDetail insertFoodDetail(FoodDetail foodDetail);
}
