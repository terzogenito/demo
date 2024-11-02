package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "banners")
public class BannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "banner_name", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String bannerName;

    @Column(name = "banner_image", nullable = false)
    private String bannerImage;

    @Column(name = "description")
    private String description;

    public BannerEntity(Long id, @NotBlank @Size(max = 100) String bannerName, String bannerImage, String description) {
        this.id = id;
        this.bannerName = bannerName;
        this.bannerImage = bannerImage;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
