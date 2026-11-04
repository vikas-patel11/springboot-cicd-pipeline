package com.springboot_cicdpipeline.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/welcome")
public class WelcomeController {


    @GetMapping("/jenkin")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("jenkin and docker   denjbev");
    }

    @GetMapping("/name")
    public ResponseEntity<String> showName() {
        return ResponseEntity.ok("viaks patel de do");
    }
}
