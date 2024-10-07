package me.pgthinker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project: me.pgthinker.controller
 * @Author: De Ning
 * @Date: 2024/10/7 23:35
 * @Description:
 */
@RestController
public class CheckController {

    @GetMapping("/check")
    public String check() {
        return "Hello World";
    }
}
