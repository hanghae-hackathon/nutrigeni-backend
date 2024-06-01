package org.nutrigenie.technopark.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ValueGenerationType;
import org.nutrigenie.technopark.dto.UploadFoodDTO;
import org.nutrigenie.technopark.model.FoodDetail;
import org.nutrigenie.technopark.model.UploadFood;
import org.nutrigenie.technopark.service.UploadFoodService;
import org.nutrigenie.technopark.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileUploadController {

    @Value("${uploadImagePath}")
    String uploadDir;

    @Value("${pythonExportPath}")
    public String pythonExportPath;

    @Value("${openaiKey}")
    public String openaiKey;

    private final UploadFoodService uploadFoodService;

    @PostMapping("/image-upload")
    public ResponseEntity<String> handleFileUpload(@RequestBody MultipartFile file) {

        String uploadFilePath = uploadDir + "/" + file.getOriginalFilename();

        try {
            Path copyLocation = Paths.get(uploadFilePath);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            String pythonSource = Util.foodImageAnalysisPythonExport(uploadFilePath, openaiKey);
            String resultFoodImageAnalysisData = Util.foodImageAnalysisPythonStart(file.getOriginalFilename().split("\\.")[0], pythonSource, pythonExportPath);

            Map<String, Object> foodAnalysisDataMap = Util.jsonToMap(resultFoodImageAnalysisData);

            UploadFood uploadFood = new UploadFood();

            uploadFood.setImagePath(file.getOriginalFilename());
            uploadFood.setFoodName((String) foodAnalysisDataMap.get("음식이름"));
            uploadFood.setTotalCalories((Integer) foodAnalysisDataMap.get("총칼로리"));

            uploadFoodService.insertFoodAnalysisData(uploadFood);


            JSONObject jsonObject = new JSONObject(foodAnalysisDataMap);
            JSONArray jsonArray = jsonObject.getJSONArray("재료");
            for (int i = 0; i < jsonArray.length(); i++) {
                FoodDetail foodDetail = new FoodDetail();
                JSONObject jsonObjectTemp = jsonArray.getJSONObject(i);

                foodDetail.setAnalysisName(jsonObjectTemp.get("이름").toString());

                JSONObject objectTemp = jsonObjectTemp.getJSONObject("영양분");
                foodDetail.setCalories(Integer.parseInt(jsonObjectTemp.get("칼로리").toString()));
                foodDetail.setProtein(objectTemp.get("단백질").toString());
                foodDetail.setCarbs(objectTemp.get("탄수화물").toString());
                foodDetail.setFat(objectTemp.get("지방").toString());
                foodDetail.setUploadFood(uploadFood);
                uploadFoodService.insertFoodDetail(foodDetail);

            }


            return ResponseEntity.ok(resultFoodImageAnalysisData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file: " + file.getOriginalFilename() + "!");
        }
    }
}
