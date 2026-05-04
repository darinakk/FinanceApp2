package com.example.finance2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    int x = 0;
    @GetMapping("/test")
    public String test() {
        return "API works hejjjjjj";
    }
}
