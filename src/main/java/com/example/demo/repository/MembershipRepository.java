package com.example.demo.repository;

import com.example.demo.entity.MembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<MembershipEntity, Long> {
    MembershipEntity findByEmail(String email);
    boolean existsByEmail(String email);
}
