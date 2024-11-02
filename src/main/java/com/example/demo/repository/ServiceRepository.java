package com.example.demo.repository;

import com.example.demo.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    ServiceEntity findByServiceCode(String code);
}

