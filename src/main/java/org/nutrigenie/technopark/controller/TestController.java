package org.nutrigenie.technopark.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "테스트 컨트롤러")
public class TestController {


    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
