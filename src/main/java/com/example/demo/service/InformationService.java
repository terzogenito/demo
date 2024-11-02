package com.example.demo.service;

import com.example.demo.entity.BannerEntity;
import com.example.demo.entity.ServiceEntity;
import com.example.demo.repository.BannerRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Service
public class InformationService {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public ResponseEntity getBanners() {
        List<BannerEntity> banners = bannerRepository.findAll();
        return ResponseEntity.ok(banners);
    }

    public ResponseEntity getServices() {
        List<ServiceEntity> services = serviceRepository.findAll();
        return ResponseEntity.ok(services);
    }
}
