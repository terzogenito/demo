package com.example.demo.repository;

import com.example.demo.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<BannerEntity, Long> {
}
