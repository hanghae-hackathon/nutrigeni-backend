package org.nutrigenie.technopark.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nutrigenie.technopark.model.FoodDetail;
import org.nutrigenie.technopark.model.UploadFood;
import org.nutrigenie.technopark.service.FoodService;
import org.nutrigenie.technopark.service.UploadFoodService;
import org.nutrigenie.technopark.service.UserService;
import org.nutrigenie.technopark.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodController {

    private final OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor;
    @Value("${pythonExportPath}")
    String pythonExportPath;

    @Value("${uploadImagePath}")
    String uploadImagePath;

    private final UploadFoodService uploadFoodService;
    private final FoodService foodService;
    private final UserService userService;

    @GetMapping("/upload-food")
    public List<UploadFood> uploadFoodData() {
        return uploadFoodService.uploadFoodData();
    }

    @GetMapping("/food-exercise-method")
    public String foodExerciseMethod(@RequestParam("foodName") String foodName) {
        return foodService.foodExerciseMethod(foodName);
    }

    @GetMapping("/check-health-food-conflicts")
    public String checkHealthAndFoodConflicts(@RequestParam("email") String email,
                                              @RequestParam("foodName") String foodName){

        String medicalConditions =  userService.findMedicalConditions(email);

        return foodService.checkHealthAndFoodConflicts(foodName, medicalConditions);
    }

    @GetMapping("/food-details")
    public List<FoodDetail> foodDetailData(@RequestParam("id") String id) {

        return foodService.foodDetailData(id);
    }

}
