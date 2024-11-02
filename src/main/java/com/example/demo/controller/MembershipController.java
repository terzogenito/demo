package com.example.demo.controller;

import com.example.demo.entity.MembershipEntity;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegistrationRequest;
import com.example.demo.model.ProfileUpdateRequest;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.MembershipService;
import com.example.demo.service.TransactionService;
import com.example.demo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MembershipController {

    private final MembershipService membershipService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MembershipController(
            MembershipService membershipService,
            JwtUtil jwtUtil
    ) {
        this.membershipService = membershipService;
        this.jwtUtil = jwtUtil;
    }

    @RequestMapping(value="/registration", headers="Accept=application/json", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest request) {
        boolean isRegistered = membershipService.register(request);
        if (isRegistered) {
            ApiResponse response = new ApiResponse(0, "Registrasi berhasil silahkan login", null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(102, "Parameter email tidak sesuai format", null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {
        return membershipService.login(loginRequest);
    }

    @GetMapping("/profile")
    public ApiResponse getProfile(@RequestHeader("Authorization") String token) {
        Claims claims = jwtUtil.validateToken(token);
        String email = jwtUtil.extractUsername(token);
        MembershipEntity profile = membershipService.getProfileByEmail(email);
        if (profile == null) {
            return new ApiResponse(103, "Profile not found", null);
        }
        return new ApiResponse(0, "Sukses", profile);
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateProfile(
        @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest,
        Authentication authentication
    ) {
        try {
            String email = authentication.getName();
            membershipService.updateProfile(email, profileUpdateRequest);
            return ResponseEntity.ok(new ApiResponse(0, "Profile updated successfully", null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(108, "Token tidak valid atau kadaluwarsa", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Internal Server Error", null));
        }
    }

    @PutMapping(value = "/profile/image", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProfileImage(
        @RequestParam("profileImage") MultipartFile profileImage,
        Authentication authentication
    ) {
        try {
            if (profileImage.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, "Profile image file is required", null));
            }
            String email = authentication.getName();
            String imageUrl = membershipService.updateProfileImage(email, profileImage);
            Map<String, String> data = new HashMap<>();
            data.put("profileImageUrl", imageUrl);
            return ResponseEntity.ok(new ApiResponse(0, "Profile image updated successfully", data));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(108, "Token tidak valid atau kadaluwarsa", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Internal Server Error", null));
        }
    }
}
