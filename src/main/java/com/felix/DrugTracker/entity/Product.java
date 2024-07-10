package com.felix.DrugTracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String serialNumber;
    private String batchNumber;
    private String uniqueId;

    private String qrCode;

    // Constructor

    public Product(){

    }
    public Product(String name, String serialNumber, String batchNumber) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.batchNumber = batchNumber;
        this.uniqueId = generateUniqueId(name, serialNumber, batchNumber);
    }

    // Method to generate unique ID
    private String generateUniqueId(String name, String serialNumber, String batchNumber) {
        System.out.println(this);

        String uniqueId = name + "-" + serialNumber + "-" + batchNumber + "-" + UUID.randomUUID();
        System.out.println("The unique id generated is " + uniqueId);
        return uniqueId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        if (uniqueId == null || uniqueId.isEmpty()) {
            this.uniqueId = generateUniqueId(this.name, this.serialNumber, this.batchNumber);
        } else {
            this.uniqueId = uniqueId;
        }
    }

    public void setQrCode(String qrCodePath) {
        this.qrCode = qrCodePath;
    }

    public String getQrCode(){
        return qrCode;
    }
}
