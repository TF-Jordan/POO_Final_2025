package com.GestionEvenement.GestionEvenement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("/api/test")
@RestController
@RequiredArgsConstructor
public class testController {


    @PostMapping("post")
    public ResponseEntity<?> testPost(){
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("test","test");
        return ResponseEntity.ok(response);
    }
}
