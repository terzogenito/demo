package com.example.demo.service;

import com.example.demo.entity.MembershipEntity;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegistrationRequest;
import com.example.demo.model.ProfileUpdateRequest;
import com.example.demo.repository.MembershipRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @Autowired
    public MembershipService(MembershipRepository membershipRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.membershipRepository = membershipRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public boolean register(RegistrationRequest request) {
        if (membershipRepository.existsByEmail(request.getEmail())) {
            return false;
        }
        createNewMember(
            1L,
            request.getEmail(),
            request.getFirstName(),
            request.getLastName(),
            request.getPassword(),
            0,
            null
        );
        return true;
    }

    public MembershipEntity createNewMember(Long id, String email, String firstName, String lastName, String password, double balance, String profileImage) {
        String encodedPassword = passwordEncoder.encode(password);
        MembershipEntity newMember = new MembershipEntity(id, email, firstName, lastName, encodedPassword, balance, profileImage);
        return membershipRepository.save(newMember);
    }

    public ApiResponse login(LoginRequest loginRequest) {
        MembershipEntity membership = membershipRepository.findByEmail(loginRequest.getEmail());
        if (membership == null || !passwordEncoder.matches(loginRequest.getPassword(), membership.getPassword())) {
            return new ApiResponse(103, "Username atau password salah", null);
        }
        String token = jwtUtil.generateToken(membership);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("token", token);
        return new ApiResponse(0, "Login successful", responseData);
    }

    public MembershipEntity getProfileByEmail(String email) {
        MembershipEntity membership = membershipRepository.findByEmail(email);
        if (membership == null) {
            return new MembershipEntity();
        } else {
            return membership;
        }
    }

    public void updateProfile(String userEmail, ProfileUpdateRequest profileUpdateRequest) {
        MembershipEntity membership = membershipRepository.findByEmail(userEmail);
        if (membership == null) {
            throw new UsernameNotFoundException("User not found");
        }
        membership.setFirstName(profileUpdateRequest.getFirstName());
        membership.setLastName(profileUpdateRequest.getLastName());
        membershipRepository.save(membership);
    }

    public String updateProfileImage(String userEmail, MultipartFile profileImage) throws IOException {
        MembershipEntity membership = membershipRepository.findByEmail(userEmail);
        if (membership == null) {
            throw new UsernameNotFoundException("User not found");
        }
        String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
        Path filePath = Paths.get("src/main/resources/profile-image/", fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, profileImage.getBytes());
        membership.setProfileImage("/" + filePath.toString());
        membershipRepository.save(membership);
        return "/" + filePath.toString();
    }

    public double getBalanceByEmail(String userEmail) {
        MembershipEntity membership = membershipRepository.findByEmail(userEmail);
        if (membership == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return membership.getBalance();
    }

    public Long getUserIdByEmail(String userEmail) {
        MembershipEntity membership = membershipRepository.findByEmail(userEmail);
        return membership.getId();
    }
}
