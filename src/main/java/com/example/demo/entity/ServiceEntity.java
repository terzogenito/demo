package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "services")
public class ServiceEntity {

    @Id
    @Column(name = "service_code", nullable = false, unique = true)
    @NotBlank
    private String serviceCode;

    @Column(name = "service_tariff", nullable = false)
    @NotBlank
    private Double serviceTariff;

    @Column(name = "transaction_type", nullable = false)
    @NotBlank
    @Size(max = 50)
    private String transactionType;

    @Column(name = "service_name", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String serviceName;

    @Column(name = "service_icon")
    private String serviceIcon;

    @Column(name = "description")
    private String description;

    public ServiceEntity(
        @NotBlank String serviceCode,
        @NotBlank Double serviceTariff,
        @NotBlank @Size(max = 50) String transactionType,
        @NotBlank @Size(max = 100) String serviceName,
        String serviceIcon,
        String description
    ) {
        this.serviceCode = serviceCode;
        this.serviceTariff = serviceTariff;
        this.transactionType = transactionType;
        this.serviceName = serviceName;
        this.serviceIcon = serviceIcon;
        this.description = description;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Double getServiceTariff() {
        return serviceTariff;
    }

    public void setServiceTariff(Double serviceTariff) {
        this.serviceTariff = serviceTariff;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceIcon() {
        return serviceIcon;
    }

    public void setServiceIcon(String serviceIcon) {
        this.serviceIcon = serviceIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
