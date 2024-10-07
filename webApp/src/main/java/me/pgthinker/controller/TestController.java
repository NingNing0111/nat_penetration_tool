package me.pgthinker.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project: me.pgthinker.controller
 * @Author: De Ning
 * @Date: 2024/10/3 22:15
 * @Description:
 */
@RestController
public class TestController {

    @GetMapping("/check")
    public String check() {
        return "Hello World!";
    }

}
