package org.nutrigenie.technopark.controller;

import lombok.RequiredArgsConstructor;
import org.nutrigenie.technopark.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodController {

    @Value("${pythonExportPath}")
    String pythonExportPath;

    @Value("${uploadImagePath}")
    String uploadImagePath;

    @GetMapping("/ai-food-analysis")
    public String aiFoodAnalysis() {

        StringBuilder resultSb = new StringBuilder();

        String imagePath = uploadImagePath + "/galbe.jpeg";
        // 파일 경로
        String filePath = pythonExportPath + "/test.py"; // 저장할 파일 경로를 여기에 입력하세요

        // 텍스트를 파일로 저장
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(Util.foodImageAnalysisPythonExport(imagePath));
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
}
