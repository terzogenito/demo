package com.example.demo.controller;

import com.example.demo.entity.MembershipEntity;
import com.example.demo.model.TopUpRequest;
import com.example.demo.model.TransactionRequest;
import com.example.demo.response.ApiResponse;
import com.example.demo.response.TransactionHistoryData;
import com.example.demo.response.TransactionHistoryResponse;
import com.example.demo.service.MembershipService;
import com.example.demo.service.TransactionService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {

    private final MembershipService membershipService;
    private final TransactionService transactionService;
    private final JwtUtil jwtUtil;

    @Autowired
    public TransactionController(
        MembershipService membershipService,
        TransactionService transactionService,
        JwtUtil jwtUtil
    ) {
        this.membershipService = membershipService;
        this.transactionService = transactionService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse> getBalance(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponse(108, "Token tidak valid atau kadaluwarsa", null)
            );
        }
        double balance = membershipService.getBalanceByEmail(userDetails.getUsername());
        Map<String, Object> balanceData = new HashMap<>();
        balanceData.put("balance", balance);
        ApiResponse response = new ApiResponse(0, "Get Balance Berhasil", balanceData);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/topup")
    public ResponseEntity<ApiResponse> topUpBalance(
        @RequestHeader("Authorization") String token,
        @RequestBody TopUpRequest topUpRequest
    ) {
        String userEmail;
        try {
            userEmail = jwtUtil.extractUsername(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(108, "Token tidak valid atau kadaluwarsa", null));
        }
        if (topUpRequest.getTopUpAmount() == null || topUpRequest.getTopUpAmount() <= 0) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(102, "Parameter amount hanya boleh angka dan tidak boleh lebih kecil dari 0", null));
        }
        ApiResponse response = transactionService.topUpBalance(userEmail, topUpRequest.getTopUpAmount());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction")
    public ResponseEntity<ApiResponse> makeTransaction(
        @RequestHeader("Authorization") String token,
        @RequestBody TransactionRequest transactionRequest
    ) {
        String email;
        try {
            email = jwtUtil.extractUsername(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(108, "Token tidak valid atau kadaluwarsa", null));
        }
        try {
            ApiResponse response = transactionService.makeTransaction(email, transactionRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(102, "Service atau Layanan tidak ditemukan", null));
        }
    }

    @GetMapping("/transaction/history")
    public ResponseEntity<ApiResponse> getTransactionHistory(
        @RequestHeader("Authorization") String authHeader,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "3") int limit
    ) {
        String token = authHeader.replace("Bearer ", "");
        if (!jwtUtil.isTokenValidate(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(108, "Token tidak tidak valid atau kadaluwarsa", null));
        }
        String userEmail = jwtUtil.extractUsername(token);
        List<TransactionHistoryResponse> transactionHistory = transactionService.getTransactionHistory(userEmail, offset, limit);
        TransactionHistoryData data = new TransactionHistoryData(offset, limit, transactionHistory);
        ApiResponse response = new ApiResponse(0, "Get History Berhasil", data);
        return ResponseEntity.ok(response);
    }
}
