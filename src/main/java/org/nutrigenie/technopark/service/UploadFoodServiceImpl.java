package org.nutrigenie.technopark.service;

import lombok.RequiredArgsConstructor;
import org.nutrigenie.technopark.dto.UploadFoodDTO;
import org.nutrigenie.technopark.model.FoodDetail;
import org.nutrigenie.technopark.model.UploadFood;
import org.nutrigenie.technopark.repository.FoodDetailRepository;
import org.nutrigenie.technopark.repository.UploadFoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadFoodServiceImpl implements UploadFoodService {

    private final UploadFoodRepository uploadFoodRepository;
    private final FoodDetailRepository foodDetailRepository;

    @Override
    public UploadFood insertFoodAnalysisData(UploadFood uploadFood) {

        return uploadFoodRepository.save(uploadFood);

    }

    @Override
    public List<UploadFood> uploadFoodData() {

        return uploadFoodRepository.uploadFoodData();
    }

    @Override
    public FoodDetail insertFoodDetail(FoodDetail foodDetail) {
        return foodDetailRepository.save(foodDetail);
    }
}
