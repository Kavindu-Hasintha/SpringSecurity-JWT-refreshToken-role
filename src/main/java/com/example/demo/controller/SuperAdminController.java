package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/super-admin")
public class SuperAdminController {

    @GetMapping
    public ResponseEntity<String> superAdmin() {
        return ResponseEntity.ok("Hi, I'm super admin, Only admin role can access this point");
    }
}
