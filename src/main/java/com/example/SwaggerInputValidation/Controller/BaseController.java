package com.example.SwaggerInputValidation.Controller;

import com.example.SwaggerInputValidation.Service.SampleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BaseController {

    @Autowired
    private SampleService sampleService;

    @GetMapping(value = "/concatStrings")
    public String concatNames(HttpServletRequest request, @RequestParam String firstString, @RequestParam String secondString) {
        return sampleService.concat(request, firstString, secondString);
    }

    @PostMapping(value = "/createUser")
    public ResponseEntity<?> createUser(HttpServletRequest request, @RequestBody String body) {
        return sampleService.createUser(request, body);
    }
}
