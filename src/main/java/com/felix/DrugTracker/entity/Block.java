package com.felix.DrugTracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.felix.DrugTracker.util.StringUtil;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String previousHash;
    private String data;
    private long timeStamp;
    private String hash;

    @ManyToOne
    @JoinColumn(name = "blockchain_id")
    @JsonIgnoreProperties("blocks")
    private Blockchain blockchain;

    public Block(){

    }

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }



    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(previousHash + Long.toString(timeStamp) + data);
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

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }
}
