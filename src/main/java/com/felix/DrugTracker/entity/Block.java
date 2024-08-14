package com.felix.DrugTracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.felix.DrugTracker.util.StringUtil;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String previousHash;
    private String data;
    private LocalDateTime timeStamp;
    private String hash;

    private String userRole;

    @ManyToOne
    @JoinColumn(name = "blockchain_id")
    @JsonIgnoreProperties("blocks")
    private Blockchain blockchain;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // Added field

    public Block(){

    }

    public Block(String data, String previousHash, User user) {
        this.data = data;
        this.previousHash = previousHash;
        this.user = user;
        this.timeStamp = LocalDateTime.now();
        this.hash = calculateHash();
        setUserRole();

    }



    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(previousHash + timeStamp + data);
        return calculatedHash;
    }

    public String getHash() {
        return hash;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole() {
        this.userRole = user.getRole();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }
}
