package com.example.demo.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.service.InformationService;
import com.example.demo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InformationController {

    private final InformationService informationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public InformationController(InformationService informationService, JwtUtil jwtUtil) {
        this.informationService = informationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/banner")
    public ResponseEntity<ApiResponse> getBanners(@RequestHeader("Authorization") String token) {
        Claims claims = jwtUtil.validateToken(token.replace("Bearer ", ""));
        var banners = informationService.getBanners();
        ApiResponse response = new ApiResponse(0, "Sukses", banners);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/services")
    public ResponseEntity<ApiResponse> getServices(@RequestHeader("Authorization") String token) {
        Claims claims = jwtUtil.validateToken(token.replace("Bearer ", ""));
        var services = informationService.getServices();
        ApiResponse response = new ApiResponse(0, "Sukses", services);
        return ResponseEntity.ok(response);
    }
}
