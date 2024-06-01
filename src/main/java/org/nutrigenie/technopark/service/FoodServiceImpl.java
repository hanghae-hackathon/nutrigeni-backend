package org.nutrigenie.technopark.service;

import lombok.RequiredArgsConstructor;
import org.nutrigenie.technopark.model.FoodDetail;
import org.nutrigenie.technopark.repository.FoodDetailRepository;
import org.nutrigenie.technopark.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService{

    @Value("${openaiKey}")
    public String openaiKey;

    @Value("${pythonExportPath}")
    public String pythonExportPath;

    private final FoodDetailRepository foodDetailRepository;

    @Override
    public String foodExerciseMethod(String foodName) {
        StringBuilder resultSb = new StringBuilder();

        String foodExerciseMethodSource = Util.foodExerciseMethodPythonExport(openaiKey, foodName);

        // 파일 경로
        String filePath = pythonExportPath + "/" + foodName + ".py"; // 저장할 파일 경로를 여기에 입력하세요

        // 텍스트를 파일로 저장
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(foodExerciseMethodSource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 파이썬 프로세스 빌드
            ProcessBuilder pb = new ProcessBuilder("python", filePath);
            Process process = pb.start();

            // 프로세스의 표준 출력을 읽기 위한 BufferedReader
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 프로세스의 표준 오류를 읽기 위한 BufferedReader
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // 표준 출력 출력
            String s;
            while ((s = stdInput.readLine()) != null) {
                resultSb.append(s).append("\n");
            }

            // 표준 오류 출력
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
            }

            // 프로세스가 종료될 때까지 대기
            int exitCode = process.waitFor();
            System.out.println("Exited with code : " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return resultSb.toString();
    }

    @Override
    public String checkHealthAndFoodConflicts(String foodName, String medicalConditions) {

        StringBuilder resultSb = new StringBuilder();

        String pythoneSource = Util.checkHealthAndFoodConflictsPythonExport(foodName, medicalConditions, openaiKey);
        // 파일 경로
        String filePath = pythonExportPath + "/" + foodName + "_" + medicalConditions + ".py"; // 저장할 파일 경로를 여기에 입력하세요

        // 텍스트를 파일로 저장
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(pythoneSource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 파이썬 프로세스 빌드
            ProcessBuilder pb = new ProcessBuilder("python", filePath);
            Process process = pb.start();

            // 프로세스의 표준 출력을 읽기 위한 BufferedReader
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 프로세스의 표준 오류를 읽기 위한 BufferedReader
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // 표준 출력 출력
            String s;
            while ((s = stdInput.readLine()) != null) {
                resultSb.append(s).append("\n");
            }

            // 표준 오류 출력
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
            }

            // 프로세스가 종료될 때까지 대기
            int exitCode = process.waitFor();
            System.out.println("Exited with code : " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return resultSb.toString();

    }

    @Override
    public List<FoodDetail> foodDetailData(String foodId) {

        return foodDetailRepository.foodDetailData(foodId);

    }
}
