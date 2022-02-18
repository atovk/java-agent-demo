package org.atovk.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DemoController {

    @GetMapping("demo")
    public String demo() {
        return "hello world api resp!!!";
    }
}
