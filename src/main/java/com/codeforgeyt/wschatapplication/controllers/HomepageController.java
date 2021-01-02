package com.codeforgeyt.wschatapplication.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomepageController {

    @GetMapping(value = "/", produces = "application/json")
    public String hello() {
        return "Hello World";
    }

}
